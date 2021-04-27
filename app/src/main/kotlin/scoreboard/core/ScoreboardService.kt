package scoreboard.core

interface ScoreboardService {
    fun getAllPlayerRanks(): List<RankedPlayer>
    fun getPlayerRank(pseudo: String): RankedPlayer?
    fun getPlayer(pseudo: String): Player?
    fun savePlayer(player: Player): Player
    fun emptyScoreboard(): List<RankedPlayer>
}