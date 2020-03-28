import junit.framework.Assert
import org.junit.Test

class MonteCarloTests {
    @Test
    fun `Overall test`() {
        val state = startingState()
        var carlo = MonteCarloTreeSearch(state)
        print(carlo.findNextMove(Winner.ONE))
        val stop = 5;
    }
}