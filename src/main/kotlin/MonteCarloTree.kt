
/**
 * Node for Monte Carlo Tree
 */
class Node(val state: State, var parent: Node?, var move: Move?) {
    lateinit var children: List<Node>
    var instantLoss = false
    var visits: Int = 0
    var wins: Int = 0
    var uctValue: Double = Double.MAX_VALUE
    fun getPlayer(): Int = getPlayer(state)
    fun getChildByMove(move: Move) = children.firstOrNull { it.move!!.equals(move) }
    fun winRatio() = if (instantLoss || visits <= 0) -1.0 else wins.toDouble() / visits.toDouble()

    var expanded = false
}

class Tree(var root: Node)


/**
 * Heuristic for choosing which nodes to expand
 */
// UCT = Upper Confidence Bound 1 applied to trees
fun calculateUctValue(totalParentVisit: Int, node: Node): Double {
    return when {
        node.instantLoss -> -1.0
        node.visits == 0 -> Double.MAX_VALUE // we want to visit unexplored ones first
        else -> node.winRatio() + 1.41 * Math.sqrt(Math.log(totalParentVisit.toDouble()) / node.visits.toDouble())
    }
}

fun findBestNodeWithUCT(node: Node): Node {
    return node.children.maxBy { it.uctValue }!!
}


fun findBestChildNode(node: Node): Node {
    // ta co je toto za bullshit // return node.children.maxBy { it.visits }!!
    return node.children.maxBy { it.winRatio() }!!
}