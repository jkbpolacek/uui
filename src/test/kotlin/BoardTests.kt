import junit.framework.Assert.assertEquals
import org.junit.Test


class BoardTests {
    @Test
    fun `Board moves 1`() {
        val state = BoardImpl.startingState()
        val move = Move(0, 0)
        val expectedState = State(arrayOf(
                1,0, 0,0, 0,0,
                0,0, 0,0, 0,0,
                0,0, 0,0, 0,0,
                0,0, 0, 2))

        val newState = BoardImpl.playMove(move, state)
        assertEquals(newState, expectedState)
    }

    @Test
    fun `Board moves 2`() {
        val state = BoardImpl.startingState()

        val move = Move(0, 1)
        val expectedState = State(arrayOf(
                0, 0, 1, 0, 0, 0,
                0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0,
                0, 0, 0, 2))

        val newState = BoardImpl.playMove(move, state)
        val newState2 = BoardImpl.playMove(move, state)



        assertEquals(newState, expectedState)


        println(newState.hashCode())
        println(newState2.hashCode())
        val aset = hashSetOf(newState, newState2)
        println(aset.size)
        assertEquals(newState, newState2)
    }

    @Test
    fun `Board moves 3`() {
        val state = BoardImpl.startingState()


        val move = Move(1, 2)
        val expectedState = State(arrayOf(
                0, 0, 0, 0, 2, 0,
                0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0,
                0, 0, 1, 2))

        val newState = BoardImpl.playMove(move, state)
        assertEquals(newState, expectedState)
    }
}
