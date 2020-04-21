import org.junit.Test

class MonteCarloTests {
    @Test
    fun `Overall test`() {
        val state = startingState()
        val carlo = MonteCarloTreeSearch(state, Winner.TWO)
        val move = carlo.findNextMove(900L)
        println(move.let { "pos=${it.pos} boardpos=${it.boardpos}" })
        println("Simulated ${carlo.tree.root.visits} games")
        println("Wins ${carlo.tree.root.wins}")
        carlo.moveTree(move)
        println("Best move Simulated ${carlo.tree.root.visits} games")
        println("Best move Wins ${carlo.tree.root.wins}")

    }
}

/*

class MonteCa444rlo(var currentState: State) {

    private val plays: MutableMap<State, Int> = mutableMapOf()
    private val wins: MutableMap<State, Int> = mutableMapOf()

    private fun winToPlayRation(state: State): Double {
        return (wins[state] ?: 0).toDouble() / (plays[state] ?: 1)
    }

    private fun runSimulation() {
        var state = currentState
        var victor: Winner = Winner.NONE


        val visitedStates: MutableSet<State> = hashSetOf()
        var expand = true

        mainLoop@ for (t in 0..MAX_MOVES) {

            val legalNextStates = possibleMoves(state).map { playMove(it, state) }

            // if we have statistics for each possible state, pick one according to a formula, otherwise pick randomly
            val play = if (legalNextStates.all { visitedStates.contains(it) }) {
                val logTotal = ln(legalNextStates.sumBy { plays[it] ?: 0 }.toDouble())

                legalNextStates.maxBy {
                    winToPlayRation(it) + 1.4 * sqrt(logTotal / (plays[it] ?: 1))
                }?.previousMove ?: randomMove(state) ?: break@mainLoop

            } else {
                randomMove(state) ?: break@mainLoop
            }

            state = playMove(play, state)

            if (expand && plays[state] == null) {
                expand = false
                plays[state] = 0
                wins[state] = 0
            }

            visitedStates.add(state)

            victor = won(state)
            if (victor != Winner.NONE) break@mainLoop
        }

        visitedStates.forEach {
            if (plays.contains(it)) {
                plays[it] = plays[it]!! + 1
                if (victor == Winner.ONE) {
                    wins[it] = wins[it]!! + 1
                }
            }
        }
    }

    fun getNextPlay(maxTimeMillis: Long): Move {

            var games = 0

            val clock = Clock.systemUTC()
            val begin = clock.instant().toEpochMilli()
            while (clock.instant().toEpochMilli() - begin < maxTimeMillis) {
                runSimulation()
                games += 1
            }

            totalGames += games
            System.err.println("Ran $games games, total $totalGames")

            // Pick the move with the highest percentage of wins.
            val bestMove = legal.maxBy {
                winToPlayRation(nextState(currentState, it))
            }

            return bestMove!!
        }

    companion object {
        private const val MAX_MOVES = 100
        var totalGames = 0
    }


}
*/
