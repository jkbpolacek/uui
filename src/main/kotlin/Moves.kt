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

    override fun toString(): String {
        val col = pos % 3 + (boardpos % 3) * 3
        val row = pos / 3 + (boardpos / 3) * 3 // kvoli 'div' to nie je trivialna operacia
        return "$row $col"
    }
}

fun rowcolToMove(row: Int, col: Int): Move {
    return Move(
            pos = col % 3 + (row % 3) * 3,
            boardpos = col / 3 + (row / 3) * 3
    )
}


val smallBoardToMoves =
        (0..8).map { miniboard ->
            ((0..8).map { Move(it, miniboard) }.toList())
        }.toTypedArray()

val allMoves = smallBoardToMoves.flatMap { it }.toList()


private val winningMoves = listOf(
        0b111000000,
        0b000111000,
        0b000000111,
        0b100100100,
        0b010010010,
        0b001001001,
        0b100010001,
        0b001010100
)


fun validate(move: Move, state: State): Boolean {
    return ((state.s[move.boardpos * 2] or state.s[(move.boardpos * 2) + 1]) and (1 shl move.pos)) == 0 &&
            (state.s[18] or state.s[19] or state.s[22]) and (1 shl move.boardpos) == 0 // check that boardpos is not finished

}

fun finished(board: SBoard) = winningMoves.any { it and board == it }


fun checkBoardPositionValid(boardpos: Int, state: State) = finished(state.s[boardpos * 2]).not() and
        finished(state.s[boardpos * 2 + 1]).not() and checkBoardPositionFull(boardpos, state).not()


fun checkBoardPositionFull(boardpos: Int, state: State) = (state.s[boardpos * 2] or state.s[boardpos * 2 + 1]) == 0b111111111

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

enum class Winner {
    ONE,
    TWO,
    NONE,
    FULL
}

fun winnerToInt(winner: Winner): Int {
    return when (winner) {
        Winner.ONE -> 1
        Winner.TWO -> 2
        Winner.FULL -> 3
        else -> throw IllegalArgumentException()
    }
}

fun winnerToOponent(winner: Winner): Winner {
    return when (winner) {
        Winner.ONE -> Winner.TWO
        Winner.TWO -> Winner.ONE
        else -> throw IllegalArgumentException()
    }
}
