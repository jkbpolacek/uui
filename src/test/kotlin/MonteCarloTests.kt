import org.junit.Test

class MonteCarloTests {
    @Test
    fun `Overall test`() {
        val state = startingState()
        val carlo = MonteCarloTreeSearch(state)
        val move = carlo.findNextMove(Winner.ONE, 90000L)
        println(move.let { "pos=${it.pos} boardpos=${it.boardpos}" })
        println("Simulated ${carlo.tree.root.visits} games")
        println("Wins ${carlo.tree.root.wins}")
        carlo.moveTree(move)
        println("Best move Simulated ${carlo.tree.root.visits} games")
        println("Best move Wins ${carlo.tree.root.wins}")

    }
}