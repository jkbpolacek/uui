// partially copied and then augmented from https://www.baeldung.com/java-monte-carlo-tree-search

/**
 * Monte Carlo algoritm follows - selection, expansion, back-propagation and simulation
 */

/**
 * Select node by the UCT metric
 */
private fun selectNode(rootNode: Node): Node {
    var node = rootNode
    while (node.expanded && node.children.isNotEmpty()) {
        node = findBestNodeWithUCT(node)
    }
    return node
}

/**
 * Create a child node for each possible move in this node
 */
private fun expandNode(node: Node) {
    node.expanded = true
    node.children = possibleMoves(node.state).map { Node(playMove(it, node.state), node, it) }
}

/**
 * Backpropagate simulaton's result up the tree, recalculate affected values
 */
private fun backPropagate(nodeToExplore: Node, winner: Int) {
    var tempNode: Node? = nodeToExplore
    while (tempNode != null) {
        tempNode.visits += 1
        if (3 - tempNode.getPlayer() == winner) { // vyhry zapisujeme opacne
            tempNode.wins += 1
        }
        tempNode.children.forEach { it.uctValue = calculateUctValue(tempNode!!.visits, it) }
        tempNode = tempNode.parent
    }
}

private fun simulateRandomPlayout(node: Node, opponent: Winner): Winner {
    val tempState = node.state.copy()
    var boardStatus = won(tempState)
    if (boardStatus == opponent) {
        node.parent?.wins = 0
        node.parent?.instantLoss = true
        node.wins = 0

        node.instantLoss = true
        return boardStatus
    }
    while (boardStatus == Winner.NONE) {
        val move = randomMove(tempState)
        playMoveInPlace(move!!, tempState)
        boardStatus = won(tempState)
    }
    return boardStatus
}

class MonteCarloTreeSearchBasic(startingState: State, val opponent: Winner): MCTS {

    val tree = Tree(Node(startingState, null, null))

    /**
     * We move the tree to make backpropagation faster after enemy moves
     */
    override fun moveTree(move: Move) {
        if (tree.root.expanded.not()) {
            expandNode(tree.root)
        }

        tree.root = tree.root.getChildByMove(move) ?: throw IllegalStateException("No children left")
        tree.root.parent = null
    }


    /**
     * Main method that given a timeout, will simulate moves for that much time and then return best computed move
     */
    override fun findNextMove(timeOut: Long): Move {
        val rootNode = tree.root
        if (rootNode.expanded.not()) {
            expandNode(rootNode) // shoudln't happen
            if (rootNode.children.isEmpty()) {
                throw IllegalArgumentException("No more moves possible.")
            }
        }

        // define an end time which will act as a terminating condition
        val end = System.currentTimeMillis() + timeOut

        var simulatedGames = 0
        while (true) {
            val promisingNode = selectNode(rootNode)
            expandNode(promisingNode)
            backPropagate(
                    promisingNode,
                    winnerToInt(simulateRandomPlayout(promisingNode, opponent))
            )
            simulatedGames += 1
            if (simulatedGames % 50 == 0) {
                // every 50 games we check if we have already passed the time limit
                if (System.currentTimeMillis() > end) {
                    break
                }
            }
        }
        System.err.println("Simulated $simulatedGames") // err printing results in displaying the value on codinggame.com

        return findBestChildNode(rootNode).move!!
    }

}
