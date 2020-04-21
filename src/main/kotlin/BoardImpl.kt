import java.util.*

class State(val s: Array<Int>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as State
        return Arrays.equals(s, other.s)
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(s)
    }

    fun copy(): State {
        return State(Arrays.copyOf(s, STATE_SIZE))
    }

    override fun toString(): String {
        return Arrays.toString(s)
    }

    /*
    fun toBoardString(): String {
        val result = StringBuilder()
        for (i in 0..9) {
            val player1 = s[i*2]
            var player2 = s[i *2 + 1]
            result.append("Player1 $player1 in binary ${Integer.toBinaryString(player1)}\n")
            result.append("Player2 $player2 in binary ${Integer.toBinaryString(player2)}\n")
            val subresult = ArrayList<Char>()

            for (j in 0..8) {
                when {
                    player1 and (1 shl j) == 1 shl j -> subresult.add('X')
                    player2 and (1 shl j) == 1 shl j -> subresult.add('O')
                    else ->                             subresult.add('-')
                }
                if (j % 3 == 2) {
                    subresult.add('\n')
                }
            }
            result.append(subresult.joinToString(separator=""))
            result.append("\n")
            if (i == 8) {
                result.append("-----------\nBig boards are comming \n")
            }
        }
        return result.toString()
    }
    */
}


typealias SBoard = Int

const val STATE_SIZE = 23

/*
0, 1  2, 3  4, 5
6, 7  8, 9  10,11
12,13 14,15 16,17
*/
/* 18, 19 for the big boardpos */
/* 20 for the next expected play boardpos*/
/* 21 for player */
/* 22 for draws*/

val random = Random()


// replace these for faster future

fun getPlayer(state: State): Int = state.s[21]

fun getPlayerModifier(state: State): Int = getPlayer(state) - 1

fun getSmallBoard(state: State, move: Move) = move.boardpos * 2 + getPlayerModifier(state)

fun putMoveOnSBoard(state: State, move: Move): SBoard {
    state.s[getSmallBoard(state, move)] = state.s[getSmallBoard(state, move)] or (1 shl move.pos)
    return state.s[getSmallBoard(state, move)]
}


fun possibleMoves(state: State): List<Move> {
    val nextShortBoard = state.s[20]
    return if (nextShortBoard < 0) {
        allMoves.filter { validate(it, state) }
    } else {
        smallBoardToMoves[nextShortBoard].filter { validate(it, state) }
    }
}

fun randomMove(state: State): Move? {
    return possibleMoves(state).let {
        if (it.isEmpty()) null
        else it[random.nextInt(it.size)]
    }
}

fun startingState(): State {
    return State(arrayOf(
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0, -1, 1, 0))
}

fun playMove(move: Move, state: State): State {
    val player = state.s[21]
    val newState = state.copy()

    val sBoard = putMoveOnSBoard(newState, move)

    if (finished(sBoard)) {
        newState.s[18 + player - 1] = newState.s[18 + player - 1] or (1 shl move.boardpos) // mark victory
    } else if (checkBoardPositionFull(move.boardpos, newState)) {
        newState.s[22] = newState.s[22] or (1 shl move.boardpos) // mark board is full
    }

    when {
        checkBoardPositionValid(move.pos, newState) -> newState.s[20] = move.pos
        else -> newState.s[20] = -1
    }
    newState.s[21] = 3 - newState.s[21] // handle player changing
    return newState
}


fun playMoveInPlace(move: Move, state: State) {
    val player = state.s[21]

    val sBoard = putMoveOnSBoard(state, move)

    if (finished(sBoard)) {
        state.s[18 + player - 1] = state.s[18 + player - 1] or (1 shl move.boardpos) // mark victory

    } else if (checkBoardPositionFull(move.boardpos, state)) {
        state.s[22] = state.s[22] or (1 shl move.boardpos) // mark board is full
    }

    when {
        checkBoardPositionValid(move.pos, state) -> state.s[20] = move.pos
        else -> state.s[20] = -1
    }
    state.s[21] = 3 - state.s[21] // handle player changing
}
