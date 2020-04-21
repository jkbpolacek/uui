// partially copied and then augmented from https://www.baeldung.com/java-monte-carlo-tree-search


class Node(val state: State, var parent: Node?, var move: Move?) {
    lateinit var children: List<Node>
    var instantLoss = false
    var visits: Int = 0
    var wins: Int = 0
    fun getPlayer(): Int = getPlayer(state)
    fun getChildByMove(move: Move) = children.firstOrNull { it.move!!.equals(move) }
    fun winRatio() = if (instantLoss || visits <= 0) -1.0 else wins.toDouble() / visits.toDouble()

    var expanded = false
}

class Tree(var root: Node)

// UCT = Upper Confidence Bound 1 applied to trees
fun uctValue(totalVisit: Int, node: Node): Double {
    return when {
        node.instantLoss -> -1.0
        node.visits == 0 -> Double.MAX_VALUE // we want to visit unexplored ones first
        else -> node.winRatio() + 1.41 * Math.sqrt(Math.log(totalVisit.toDouble()) / node.visits.toDouble())
    }
}

fun findBestNodeWithUCT(node: Node): Node {
    return node.children.maxBy { uctValue(node.visits, it) }!!
}


fun findBestChildNode(node: Node): Node {
    // ta co je toto za bullshit // return node.children.maxBy { it.visits }!!
    return node.children.maxBy { it.winRatio() }!!
}

private fun selectNode(rootNode: Node): Node {
    var node = rootNode
    while (node.expanded && node.children.isNotEmpty()) {
        node = findBestNodeWithUCT(node)
    }
    return node
}

private fun expandNode(node: Node) {
    node.expanded = true
    node.children = possibleMoves(node.state).map { Node(playMove(it, node.state), node, it) }
}


private fun backPropagate(nodeToExplore: Node, winner: Int) {
    var tempNode: Node? = nodeToExplore
    while (tempNode != null) {
        tempNode.visits += 1
        if (3 - tempNode.getPlayer() == winner) { // vyhry zapisujeme opacne
            tempNode.wins += 1
        }
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

class MonteCarloTreeSearch(startingState: State, val opponent: Winner) {

    val tree = Tree(Node(startingState, null, null))

    fun moveTree(move: Move) {
        if (tree.root.expanded.not()) {
            expandNode(tree.root)
        }

        tree.root = tree.root.getChildByMove(move) ?: throw IllegalStateException("No children left")
        tree.root.parent = null
    }

    fun findNextMove(timeOut: Long): Move {
        val rootNode = tree.root
        if (rootNode.expanded.not()) {
            expandNode(rootNode) // shoudln't happen
            if (rootNode.children.isEmpty()) {
                throw IllegalArgumentException("No more moves possible.")
            }
        }

        // define an end time which will act as a terminating condition
        val end = System.currentTimeMillis() + timeOut

        var i = 0
        while (System.currentTimeMillis() < end) {
            val promisingNode = selectNode(rootNode)
            expandNode(promisingNode)
            backPropagate(
                    promisingNode,
                    winnerToInt(simulateRandomPlayout(promisingNode, opponent))
            )
            i += 1
            if (System.currentTimeMillis() > end) {
                System.err.println("Done, ${end - System.currentTimeMillis()} left")
                break
            }
        }
        System.err.println("Simulated $i")

        return findBestChildNode(rootNode).move!!
    }

}
