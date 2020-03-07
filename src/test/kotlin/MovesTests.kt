import org.junit.Test

class MovesTests {
    @Test
    fun `New game moves`() {
        val state = BoardImpl.startingState()
        val moves = BoardImpl.possibleMoves(state)
        assert(MoveValidator.allMoves.all { moves.contains(it) })
    }

    @Test
    fun `Expect 9 moves`() {
        val state = BoardImpl.startingState()
        val playedState = BoardImpl.playMove(Move(0, 1), state)
        val moves = BoardImpl.possibleMoves(playedState)
        assert(moves.all { it.boardpos == 0 })
        assert(moves.size == 9)
    }


    @Test
    fun `Expect 8 moves`() {
        val state = BoardImpl.startingState()
        val playedState = BoardImpl.playMove(Move(0, 0), state)
        val moves = BoardImpl.possibleMoves(playedState)
        assert(moves.all { it.boardpos == 0 })
        assert(moves.size == 8)
    }
}
