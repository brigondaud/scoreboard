package scoreboard.core

class ScoreboardServiceImpl(private val repository: PlayerRepository) : ScoreboardService {
    override fun getAllPlayerRanks(): List<RankedPlayer> {
        val players = repository.findAllSortedByBestScore()
        return Scoreboard(players).computeRanks()
    }

    override fun getPlayerRank(pseudo: String): RankedPlayer? = getAllPlayerRanks().find { it.pseudo == pseudo }

    override fun getPlayer(pseudo: String): Player? = repository.findBy(pseudo)

    override fun savePlayer(player: Player): Player = repository.upsert(player)

    override fun emptyScoreboard(): List<RankedPlayer> {
        val deletedPlayer = getAllPlayerRanks()
        repository.deleteAll()
        return deletedPlayer
    }
}