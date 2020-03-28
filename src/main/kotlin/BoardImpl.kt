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


    // val moveMap: Map<Pair<State, Move>, State> = HashMap()
    // consider later

    //val knownStates = ArrayList<State>(1000000)
    // private val stateMapper = null

    private val random = Random()


    // replace these for faster future

    fun getPlayer(state: State): Int = state.s[21]

    fun getPlayerModifier(state:State): Int = getPlayer(state) - 1

    fun getSBoardLocation(state: State, move: Move) = move.boardpos * 2 + getPlayerModifier(state)

    fun putMoveOnSBoard(state: State, move: Move): SBoard  {
        state.s[getSBoardLocation(state, move)] = state.s[getSBoardLocation(state, move)] or (1 shl move.pos)
        return state.s[getSBoardLocation(state, move)]
    }


    fun possibleMoves(state: State): List<Move> {
        val nextShortBoard = state.s[20]
        val result = (if (nextShortBoard < 0) MoveValidator.allMoves.filter { MoveValidator.validate(it, state) }
        else MoveValidator.sBoardToMoves[nextShortBoard]).filter { MoveValidator.validate(it, state) }
        return result
    }

    fun randomMove(state: State): Move? {
        return possibleMoves(state).let {
            if (it.isEmpty()) null
            else it[random.nextInt(it.size)]
        }
    }

    fun startingState(): State {
        return State(arrayOf(
                0,0, 0,0, 0,0,
                0,0, 0,0, 0,0,
                0,0, 0,0, 0,0,
                0,0, -1, 1, 0))
    }

    fun playMove(move: Move, state: State): State {
        val player = state.s[21]
        val newState = state.copy()

        val sBoard = putMoveOnSBoard(newState, move)

        if (MoveValidator.finished(sBoard)) {
            newState.s[18 + player - 1] = newState.s[18 + player - 1] or (1 shl move.boardpos) // mark victory
            newState.s[22] = newState.s[22] or (1 shl move.boardpos)
        }
        else if (MoveValidator.checkBoardPositionValid(move.boardpos, state).not()) {
            newState.s[22] = newState.s[22] or (1 shl move.boardpos) // mark board is full
        }

        when {

            MoveValidator.checkBoardPositionValid(move.pos, newState) -> newState.s[20] = move.pos
            else -> newState.s[20] = -1
        }
        newState.s[21] = 3 - newState.s[21] // handle player changing
        return newState
    }


    fun playMoveInPlace(move: Move, state: State) {
        val player = state.s[21]

        val sBoard = putMoveOnSBoard(state, move)

        if (MoveValidator.finished(sBoard)) {
            state.s[18 + player - 1] = state.s[18 + player - 1] or (1 shl move.boardpos) // mark victory
            state.s[22] = state.s[22] or (1 shl move.boardpos)
        }
        else if (MoveValidator.checkBoardPositionValid(move.boardpos, state).not()) {
            state.s[22] = state.s[22] or (1 shl move.boardpos) // mark board is full
        }

        when {
            MoveValidator.checkBoardPositionValid(move.pos, state) -> state.s[20] = move.pos
            else -> state.s[20] = -1
        }
        state.s[21] = 3 - state.s[21] // handle player changing
    }
