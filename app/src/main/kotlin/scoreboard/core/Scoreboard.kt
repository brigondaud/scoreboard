package scoreboard.core

class Scoreboard(private val sortedPlayers: List<Player>) {
    fun computeRanks(): List<RankedPlayer> {
        return sortedPlayers.foldIndexed(mutableListOf()) { playerIndex, rankedPlayers, player ->
            val lastRanked = rankedPlayers.lastOrNull()
            val rank = lastRanked?.let { if (it.score == player.score) it.rank else playerIndex + 1 } ?: 1
            rankedPlayers.add(RankedPlayer(player, rank))
            rankedPlayers
        }
    }
}