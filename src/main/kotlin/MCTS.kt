interface MCTS {
    fun findNextMove(timeOut: Long): Move
    fun moveTree(move: Move)
}

fun compareMCTS(mctsA: (State, Winner) -> MCTS, mctsB: (State, Winner) -> MCTS, timeOutA: Long = 100, timeOutB: Long = 100) {

    // A goes first
    var wins_a_first = 0
    var wins_b_second = 0


    for (i in 0..100) {
        val currentState =  startingState()
        val playerA = mctsA(startingState(), Winner.ONE)
        val playerB = mctsB(startingState(), Winner.TWO)

        while (won(currentState) == Winner.NONE) {
            val moveA = playerA.findNextMove(timeOutA)
            playerA.moveTree(moveA)
            playerB.moveTree(moveA)
            playMoveInPlace(moveA, currentState)

            if (won(currentState) != Winner.NONE) {
                break
            }

            val moveB = playerB.findNextMove(timeOutA)
            playerA.moveTree(moveB)
            playerB.moveTree(moveB)
            playMoveInPlace(moveB, currentState)
        }

        when (won(currentState)) {
            Winner.ONE -> wins_a_first += 1
            Winner.TWO -> wins_b_second += 1
        }
    }

    // B goes first
    var wins_a_second = 0
    var wins_b_first = 0


    for (i in 0..100) {
        val currentState =  startingState()
        val playerA = mctsA(startingState(), Winner.TWO)
        val playerB = mctsB(startingState(), Winner.ONE)

        while (won(currentState) == Winner.NONE) {


            val moveB = playerB.findNextMove(timeOutB)
            playerA.moveTree(moveB)
            playerB.moveTree(moveB)
            playMoveInPlace(moveB, currentState)

            if (won(currentState) != Winner.NONE) {
                break
            }

            val moveA = playerA.findNextMove(timeOutA)
            playerA.moveTree(moveA)
            playerB.moveTree(moveA)
            playMoveInPlace(moveA, currentState)
        }

        when (won(currentState)) {
            Winner.ONE -> wins_b_first += 1
            Winner.TWO -> wins_a_second += 1
        }
    }
}