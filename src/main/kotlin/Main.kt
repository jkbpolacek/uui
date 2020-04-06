import java.util.*

const val BIG_TIMEOUT = 950
const val SMALL_TIMEOUT = 105L

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

    var thisPlayer: Winner

    val state = startingState()
    val carlo = MonteCarloTreeSearch(state)
    if (opponentRow == -1) {
        thisPlayer = Winner.ONE
        val firstMove = Move(1, 1)
        playMoveInPlace(firstMove, state)
        carlo.findNextMove(Winner.TWO, BIG_TIMEOUT) // vyuzivame cas na simulaciu akoby za druheho hraca
        println(firstMove)

    } else {
        thisPlayer = Winner.TWO
        playMoveInPlace(rowcolToMove(opponentRow, opponentCol), state)
        println(carlo.findNextMove(2, BIG_TIMEOUT))
    }


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
        println(carlo.findNextMove(thisPlayer, BIG_TIMEOUT))
    }
}