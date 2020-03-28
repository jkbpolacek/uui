import org.junit.Test

class MovesTests {
    @Test
    fun `New game moves`() {
        val state = startingState()
        val moves = possibleMoves(state)
        assert(MoveValidator.allMoves.all { moves.contains(it) })
    }

    @Test
    fun `Expect 9 moves`() {
        val state = startingState()
        val playedState = playMove(Move(0, 1), state)
        val moves = possibleMoves(playedState)
        assert(moves.all { it.boardpos == 0 })
        assert(moves.size == 9)
    }


    @Test
    fun `Expect 8 moves`() {
        val state = startingState()
        val playedState = playMove(Move(0, 0), state)
        val moves = possibleMoves(playedState)
        assert(moves.all { it.boardpos == 0 })
        assert(moves.size == 8)
    }

    @Test
    fun `Count bits test`() {
        assert(countBits(1) == 1)
        assert(countBits(2) == 1)
        assert(countBits(3) == 2)
        assert(countBits(4) == 1)
        assert(countBits(5) == 2)
        assert(countBits(6) == 2)
        assert(countBits(7) == 3)
    }
}
