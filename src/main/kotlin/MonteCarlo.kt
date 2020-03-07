class MonteCarlo(var currentState: State) {
/*
    private val plays: MutableMap<State, Int> = mutableMapOf()
    private val wins: MutableMap<State, Int> = mutableMapOf()

    private fun winToPlayRation(state: State): Double {
        return (wins[state] ?: 0).toDouble() / (plays[state] ?: 1)
    }

    private fun runSimulation() {
        var state = currentState


        val visitedStates: MutableSet<State> = hashSetOf()
        var expand = true

        mainLoop@ for (t in 0..MAX_MOVES) {

            val legalMoves = BoardApi.legalPlays(state)
            val legalNextStates = legalMoves.map { state.nextState(it) }

            // if we have statistics for each possible state, pick one according to a formula, otherwise pick randomly
            val play = if (legalNextStates.all { visitedStates.contains(it) }) {
                val logTotal = ln(legalNextStates.sumBy { plays[it] ?: 0 }.toDouble())

                legalNextStates.maxBy {
                    winToPlayRation(it) + 1.4 * sqrt(logTotal / (plays[it] ?: 1))
                }?.previousMove ?: legalMoves.shuffled().firstOrNull() ?: break@mainLoop

            } else {
                val shuffled = legalMoves.shuffled()
                shuffled.firstOrNull() ?: break@mainLoop
            }

            state = BoardApi.nextState(state, play)

            if (expand && plays[state] == null) {
                expand = false
                plays[state] = 0
                wins[state] = 0
            }

            visitedStates.add(state)

            victor = BoardApi.winner(state)
            if (victor != Victor.ONGOING) break@mainLoop
        }

        visitedStates.forEach {
            if (plays.contains(it)) {
                plays[it] = plays[it]!! + 1
                if (victor == Victor.ME) {
                    wins[it] = wins[it]!! + 1
                }
            }
        }
    }

    fun getNextPlay(maxTimeMillis: Long): Pos {

        BoardApi.run {
            val legal = legalPlays(currentState)

            if (legal.isEmpty()) {
                return Pos(0, 0)
            } else if (legal.size == 1) {
                return legal.first()
            }

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
    }

        private const val MAX_MOVES = 100
        var totalGames = 0

    */


}