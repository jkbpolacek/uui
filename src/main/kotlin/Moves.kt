/**
 * Represents a possible move by a player by the positon on small board and position on big board.
 * E.g. 0 0 is playing top left corner in the top left board, 0 1 is the top left corner in the top center board,
 * 1 0 is playing top center in top left board and so on.
 */
class Move(val pos: Int, val boardpos: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Move

        if (pos != other.pos) return false
        if (boardpos != other.boardpos) return false

        return true
    }

    override fun hashCode(): Int {
        return 31 * pos + boardpos
    }

    /**
     * Transforms move from my internal representation into the "row column" representation output desired by codinggame.com
     */
    override fun toString(): String {
        val col = pos % 3 + (boardpos % 3) * 3
        val row = pos / 3 + (boardpos / 3) * 3 // kvoli 'div' to nie je trivialna operacia
        return "$row $col"
    }
}

/**
 * Transforms "row column" representation into my internal one
 */
fun rowcolToMove(row: Int, col: Int): Move {
    return Move(
            pos = col % 3 + (row % 3) * 3,
            boardpos = col / 3 + (row / 3) * 3
    )
}

/**
 * A list of all possible moves, pregenerated to keep creating new objects to a minimum.
 */
val smallBoardToMoves =
        (0..8).map { miniboard ->
            ((0..8).map { Move(it, miniboard) }.toList())
        }.toTypedArray()

val allMoves = smallBoardToMoves.flatMap { it }.toList()


/**
 * A list of possible victory conditions, we use it to check for victory
 */
private val victoryConditions = listOf(
        0b111000000,
        0b000111000,
        0b000000111,
        0b100100100,
        0b010010010,
        0b001001001,
        0b100010001,
        0b001010100
)

/**
 * Validating for moves.
 * We check that the given position on the small board is not taken,
 * and that the given big board is not finished - that is won by either player or a draw
 */
fun validate(move: Move, state: State): Boolean {
    return ((state.s[18] or state.s[19] or state.s[22]) and (1 shl move.boardpos) == 0) &&
            ((state.s[move.boardpos * 2] or state.s[(move.boardpos * 2) + 1]) and (1 shl move.pos)) == 0

}

/**
 * We check if a small board is finished by checking if any victory condition is satisfied
 */
fun finished(board: SmallBoard) = victoryConditions.any { it and board == it }

/**
 * Checks if the next expected board constraint is valid.
 * Basically, if opposing player's move sends us to the top right corner, we check if the top right corner is not either finished or a draw,
 * and if not so, we have to play there. Otherwise we can play anywhere.
 */
fun checkBoardPositionValid(boardpos: Int, state: State) = finished(state.s[boardpos * 2]).not() and
        finished(state.s[boardpos * 2 + 1]).not() and checkBoardPositionFull(boardpos, state).not()

/**
 * Check for draws
 */
fun checkBoardPositionFull(boardpos: Int, state: State) = (state.s[boardpos * 2] or state.s[boardpos * 2 + 1]) == 0b111111111



enum class Winner {
    ONE,
    TWO,
    NONE,
    FULL
}

/**
 * convenience method
 */
fun winnerToInt(winner: Winner): Int {
    return when (winner) {
        Winner.ONE -> 1
        Winner.TWO -> 2
        Winner.FULL -> 3
        else -> throw IllegalArgumentException()
    }
}

/**
 * Check if entire game is won by anyone by checking the victory conditions against the small boards.
 */

fun won(state: State): Winner {
    return when {
        finished(state.s[18]) -> Winner.ONE
        finished(state.s[19]) -> Winner.TWO
        (state.s[18] or state.s[19] or state.s[22]) == 0b111111111 -> {
            val one = countBits(state.s[18])
            val two = countBits(state.s[19])
            when {
                one > two -> Winner.ONE
                two > one -> Winner.TWO
                else -> Winner.FULL
            }
        }
        else -> Winner.NONE
    }


}


/**
 * Convenience method for absolute bit counting
 * Used when no other victory condition is achieved, then the player with most small boards taken wins
 */

fun countBits(inNumber: Int): Int {
    var number = inNumber
    if (number == 0) {
        return number
    }

    var count = 0
    while (number != 0) {
        number = number and number - 1
        count++
    }
    return count
}

