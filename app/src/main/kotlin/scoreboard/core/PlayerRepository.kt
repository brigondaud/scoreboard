package scoreboard.core

interface PlayerRepository {
    fun findAllSortedByBestScore(): List<Player>
    fun findBy(pseudo: String): Player?
    fun upsert(player: Player): Player
    fun deleteAll()
}