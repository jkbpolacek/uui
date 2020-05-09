import java.util.*


// We use ints to represent each board, this helps us keep the code cleaner
typealias SmallBoard = Int

const val STATE_SIZE = 23

/**
 * Represents state of a game with an array
 *
 *
 *  0, 1  2, 3  4, 5
 *  6, 7  8, 9  10,11
 *  12,13 14,15 16,17
 *  each represent moves taken by player 1 and 2 respectively
 *  18, 19 is the same but for overall small board victories / big board
 *  20 for the positon of next expected small board
 *  21 for the expected next player
 *  22 for draws on small boards
 */
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



val random = Random()

/**
 * Convenince methods
 */

fun getPlayer(state: State): Int = state.s[21]

/**
* Returns the position of small board of a state specified by move and next player
*/

fun getSmallBoardPos(state: State, move: Move) = move.boardpos * 2 + getPlayer(state) - 1


/**
 * Writes move on a board and return the changed board
 */
fun putMoveOnSBoard(state: State, move: Move): SmallBoard {
    state.s[getSmallBoardPos(state, move)] = state.s[getSmallBoardPos(state, move)] or (1 shl move.pos)
    return state.s[getSmallBoardPos(state, move)]
}

/**
 * Returns a list of viable moves for a given state
 */
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

/**
 * State at game start
 */
fun startingState(): State {
    return State(arrayOf(
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0, -1, 1, 0))
}

/**
 * Given a move and a state, plays it on the board, creates a new state and
 * does all necessary checks for small board victories, draws and so on.
 * Returns ew state
 */

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

/**
 * Same as previous method but doesn't create a new state, instead modifies the current one (for optimisation).
 */

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



