package scoreboard.core

import org.junit.Test
import kotlin.test.assertEquals

class ScoreboardTest {

    @Test
    fun `scoreboard should handle no players`() {
        val scoreboard = Scoreboard(listOf())
        assertEquals(listOf(), scoreboard.computeRanks())
    }

    @Test
    fun `scoreboard should give different ranks for different scores`() {
        val scoreboard = Scoreboard(
            listOf(
                Player("first", 10),
                Player("second", 0)
            )
        )
        assertEquals(listOf(1, 2), scoreboard.computeRanks().map { it.rank })
    }

    @Test
    fun `scoreboard should give same ranks to the same scores`() {
        val scoreboard = Scoreboard(
            listOf(
                Player("first", 10),
                Player("first", 10)
            )
        )
        assertEquals(listOf(1, 1), scoreboard.computeRanks().map { it.rank })
    }

    @Test
    fun `scoreboard should skip ranks after players with the same scores`() {
        val scoreboard = Scoreboard(
            listOf(
                Player("first", 10),
                Player("second", 8),
                Player("second", 8),
                Player("fourth", 5)
            )
        )
        assertEquals(listOf(1, 2, 2, 4), scoreboard.computeRanks().map { it.rank })
    }
}