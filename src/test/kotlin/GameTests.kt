import org.junit.Test
import kotlin.system.measureTimeMillis

class GameTests {
    @Test
    fun `Board moves 1`() {
        var state = BoardImpl.startingState()
        var movesCount = 0
        while (MoveValidator.won(state) == Winner.NONE) {
            val move = BoardImpl.randomMove(state)

            state = BoardImpl.playMove(move!!, state)
            movesCount += 1
        }

        println(movesCount)
        println(state)
    }


    @Test
    fun `Measure 1 game play`() {

        var movesCount = 0
        val time = measureTimeMillis {

            var state = BoardImpl.startingState()
            while (MoveValidator.won(state) == Winner.NONE) {
                val move = BoardImpl.randomMove(state)
                state = BoardImpl.playMove(move!!, state)
                movesCount += 1
            }
        }

        println("Moves $movesCount")
        println("Time for game $time")
    }

    @Test
    fun `Measure 1000 game plays`() {

        var movesCount = 0
        val time = measureTimeMillis {

            for (i in 0..999) {
                var state = BoardImpl.startingState()
                while (MoveValidator.won(state) == Winner.NONE) {
                    val move = BoardImpl.randomMove(state)
                    if (move == null) {
                        println(movesCount)
                        println(state)
                    }

                    state = BoardImpl.playMove(move!!, state)
                    movesCount += 1
                }
                movesCount = 0
            }
        }

        println("Time for game $time") // roughly 263 ms
    }


    @Test
    fun `Measure 10000 game plays`() {

        val time = measureTimeMillis {

            for (i in 0..9999) {
                var state = BoardImpl.startingState()
                while (MoveValidator.won(state) == Winner.NONE) {
                    val move = BoardImpl.randomMove(state)
                    state = BoardImpl.playMove(move!!, state)
                }
            }
        }

        println("Time for game $time")  // expect roughly 900 ms
    }



    @Test
    fun `Measure 100000 game plays`() {

        val time = measureTimeMillis {

            for (i in 0..99999) {
                var state = BoardImpl.startingState()
                while (MoveValidator.won(state) == Winner.NONE) {
                    val move = BoardImpl.randomMove(state)
                    state = BoardImpl.playMove(move!!, state)
                }
            }
        }

        println("Time for game $time")  // expect roughly 6000 ms
    }


    @Test
    fun `Average moves in 10000 game plays`() {

        var movesCount = 0
        val time = measureTimeMillis {

            for (i in 0..9999) {
                var state = BoardImpl.startingState()
                while (MoveValidator.won(state) == Winner.NONE) {
                    val move = BoardImpl.randomMove(state)
                    state = BoardImpl.playMove(move!!, state)
                    movesCount += 1
                }
            }
        }

        println("Moves ${movesCount/10000}")
    }



    @Test
    fun `Max moves in 1000000 game plays`() {

        var maxMovesCount = 0
        val time = measureTimeMillis {

            for (i in 0..999999) {
                var movesCount = 0
                var state = BoardImpl.startingState()
                while (MoveValidator.won(state) == Winner.NONE) {
                    val move = BoardImpl.randomMove(state)
                    state = BoardImpl.playMove(move!!, state)
                    movesCount += 1
                }

                maxMovesCount = Math.max(movesCount, maxMovesCount)
            }
        }

        println("Max moves count $maxMovesCount")
    }
}
