import org.junit.Test

/**
 * Merely a test of playing out an entire game by montecarlo to check for errors
 */

class MonteCarloTests {
    @Test
    fun `Overall test`() {
        val state = startingState()
        val carlo = MonteCarloTreeSearchBasic(state, Winner.TWO)
        val move = carlo.findNextMove(900L)
        println(move.let { "pos=${it.pos} boardpos=${it.boardpos}" })
        println("Simulated ${carlo.tree.root.visits} games")
        println("Wins ${carlo.tree.root.wins}")
        carlo.moveTree(move)
        println("Best move Simulated ${carlo.tree.root.visits} games")
        println("Best move Wins ${carlo.tree.root.wins}")

    }
}