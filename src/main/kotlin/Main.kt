import java.util.*

const val BIG_TIMEOUT: Long = 950L
const val SMALL_TIMEOUT: Long = 85L

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)


    // first turn
    val opponentRow = input.nextInt()
    val opponentCol = input.nextInt()
    val validActionCount = input.nextInt()
    for (i in 0 until validActionCount) {
        input.nextInt()
        input.nextInt()
    }

    val thisPlayer: Winner

    val state = startingState()
    val carlo = MonteCarloTreeSearch(state)
    if (opponentRow == -1) {
        thisPlayer = Winner.ONE
    } else {
        thisPlayer = Winner.TWO
        playMoveInPlace(rowcolToMove(opponentRow, opponentCol), state)
    }
    val move = carlo.findNextMove(thisPlayer, BIG_TIMEOUT)
    carlo.moveTree(move)
    println(move)

    // game loop
    while (true) {
        val opponentRow = input.nextInt()
        val opponentCol = input.nextInt()
        val validActionCount = input.nextInt()
        for (i in 0 until validActionCount) {
            val row = input.nextInt()
            val col = input.nextInt()
        }

        // Write an action using println()
        // To debug: System.err.println("Debug messages...");


        carlo.moveTree(rowcolToMove(opponentRow, opponentCol))

        val move = carlo.findNextMove(thisPlayer, SMALL_TIMEOUT)
        System.err.println("Starting at ${carlo.tree.root.move}")
        System.err.println("STate ${carlo.tree.root.state}")
        System.err.println("Can play anywhere ${checkBoardPositionValid(carlo.tree.root.move!!.pos, carlo.tree.root.state)}")
        System.err.println("Valid moves ${possibleMoves(carlo.tree.root.state).joinToString()}")
        carlo.moveTree(move)
        System.err.println("Ended at ${carlo.tree.root.move}")
        println(move)
    }
}