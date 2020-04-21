import java.util.*

const val BIG_TIMEOUT: Long = 950L
const val SMALL_TIMEOUT: Long = 50L

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)


    // first turn
    var opponentRow = input.nextInt()
    var opponentCol = input.nextInt()
    var validActionCount = input.nextInt()
    for (i in 0 until validActionCount) {
        // we must accept this input but dont need it
        input.nextInt()
        input.nextInt()
    }

    val opponent: Winner
    val firstMove: Move

    val state = startingState()
    val carlo: MonteCarloTreeSearch
    if (opponentRow == -1) {
        opponent = Winner.TWO
        carlo = MonteCarloTreeSearch(state, opponent)
        firstMove = Move(4, 4)
        carlo.moveTree(firstMove)
        carlo.findNextMove(BIG_TIMEOUT)
    } else {
        opponent = Winner.ONE
        carlo = MonteCarloTreeSearch(state, opponent)
        playMoveInPlace(rowcolToMove(opponentRow, opponentCol), state)
        firstMove = carlo.findNextMove(BIG_TIMEOUT)
        carlo.moveTree(firstMove)
    }

    println(firstMove)

    // game loop
    while (true) {
        opponentRow = input.nextInt()
        opponentCol = input.nextInt()
        validActionCount = input.nextInt()
        for (i in 0 until validActionCount) {
            // we must accept this input but dont need it
            input.nextInt()
            input.nextInt()
        }

        // Write an action using println()
        // To debug: System.err.println("Debug messages...");


        carlo.moveTree(rowcolToMove(opponentRow, opponentCol))
        val nextMove = carlo.findNextMove(SMALL_TIMEOUT)
        carlo.moveTree(nextMove)
        println(nextMove)
    }
}