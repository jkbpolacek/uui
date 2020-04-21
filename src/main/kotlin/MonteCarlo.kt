// partially copied and then augmented from https://www.baeldung.com/java-monte-carlo-tree-search

class Node(val state: State, var parent: Node?, var move: Move?) {
    lateinit var children: List<Node>
    var visits: Int = 0
    var wins: Int = 0
    fun getPlayer(): Int = getPlayer(state)
    fun getRandomChild(): Node? = children[random.nextInt(children.size)]
    fun getChildByMove(move: Move) = children.firstOrNull { it.move!!.equals(move) }

    var expanded = false
}

class Tree(var root: Node)

// UCT = Upper Confidence Bound 1 applied to trees
fun uctValue(totalVisit: Int, nodeWinScore: Int, nodeVisit: Int): Double {
    return if (nodeVisit == 0) {
        Double.MAX_VALUE // we want to visit unexplored ones first
    } else nodeWinScore.toDouble() / nodeVisit.toDouble() + 1.41 * Math.sqrt(Math.log(totalVisit.toDouble()) / nodeVisit.toDouble())
}

fun findBestNodeWithUCT(node: Node): Node {
    return node.children.maxBy { uctValue(node.visits, it.wins, it.visits) }!!
}


fun findBestChildNode(node: Node): Node {
    return node.children.maxBy { if (it.visits == 0) -1.0 else it.wins.toDouble() / it.visits.toDouble() }!!
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
        if (tempNode.getPlayer() == winner) {
            tempNode.wins += 1
        }
        tempNode = tempNode.parent
    }
}

private fun simulateRandomPlayout(node: Node, opponent: Winner): Winner {
    val tempState = node.state.copy()
    var boardStatus = won(tempState)
    if (boardStatus == opponent) {
        node.wins = 0
        return boardStatus
    }
    while (boardStatus == Winner.NONE) {
        val move = randomMove(tempState)
        playMoveInPlace(move!!, tempState)
        boardStatus = won(tempState)
    }
    return boardStatus
}

class MonteCarloTreeSearch(startingState: State) {

    val tree = Tree(Node(startingState, null, null))

    fun moveTree(move: Move) {
        if (tree.root.expanded.not()) {
            expandNode(tree.root)
        }

        val child = tree.root.getChildByMove(move)
        if (child == null) {
            tree.root.children.forEach {
                System.err.println(it.move)
            }
            System.err.println("wanted $move")
        }
        tree.root = tree.root.getChildByMove(move)!!
    }

    fun findNextMove(currentPlayer: Winner, timeOut: Long): Move {
        val rootNode = tree.root
        if (rootNode.expanded.not()) {
            expandNode(rootNode) // shoudln't happen
            if (rootNode.children.isEmpty()) {
                throw IllegalArgumentException("No more moves possible.")
            }
        }

        // define an end time which will act as a terminating condition
        val end = System.currentTimeMillis() + timeOut

        System.err.println("Simulating, ${end - System.currentTimeMillis()} left")

        var i = 0
        while (System.currentTimeMillis() < end) {
            if (System.currentTimeMillis() > end) {
                System.err.println("Selecting, ${end - System.currentTimeMillis()} left")
            }
            val promisingNode = selectNode(rootNode)
            if (System.currentTimeMillis() > end) {
                System.err.println("Expanding, ${end - System.currentTimeMillis()} left")
            }
            expandNode(promisingNode)

            var nodeToExplore = promisingNode

            if (promisingNode.children.isNotEmpty()) {
                nodeToExplore = promisingNode.getRandomChild()!!
            }
            if (System.currentTimeMillis() > end) {
                System.err.println("Backpropagating, ${end - System.currentTimeMillis()} left")
            }
            backPropagate(
                    nodeToExplore,
                    winnerToInt(simulateRandomPlayout(nodeToExplore, winnerToOponent(currentPlayer)))
            )
            i+= 1
            if (System.currentTimeMillis() > end) {
                System.err.println("Done, ${end - System.currentTimeMillis()} left")
            }
        }
        System.err.println("Simulated $i")

        return findBestChildNode(rootNode).apply { System.err.println(this.wins.toDouble() / this.visits.toDouble()) }.move!!
    }

}
