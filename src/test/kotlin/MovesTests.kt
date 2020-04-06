import org.junit.Test

class MovesTests {
    @Test
    fun `New game moves`() {
        val state = startingState()
        val moves = possibleMoves(state)
        assert(allMoves.all { moves.contains(it) })
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



    @Test
    fun `Move to string test`() {
        assert(Move(0,0).toString() == "0 0")
        assert(Move(0,1).toString() == "0 3")
        assert(Move(1,1).toString() == "0 4") {(Move(1,1).toString())}
        assert(Move(0,2).toString() == "0 6")
        assert(Move(3,2).toString() == "1 6")
        assert(Move(6,2).toString() == "2 6")
        assert(Move(8,8).toString() == "8 8")
    }

    @Test
    fun `Colrow to move`() {
        assert(rowcolToMove(0, 0)== Move(0, 0))
        assert(rowcolToMove(0, 1)== Move(1, 0))
        assert(rowcolToMove(1, 0)== Move(3, 0))
        assert(rowcolToMove(1, 3)== Move(3, 1))
        assert(rowcolToMove(4, 3)== Move(3, 4)) {rowcolToMove(4, 3)}
        assert(rowcolToMove(8, 8)== Move(8, 8)) {rowcolToMove(8, 8)}
    }
}
