package scoreboard.db

import com.amazonaws.services.dynamodbv2.datamodeling.*
import scoreboard.core.Player

@DynamoDBTable(tableName = "players")
data class PlayerItem(
    // This hash key represents a drawback for sharding, but allows to query all players sorted by score,
    // instead of performing an unsorted scan.
    @DynamoDBHashKey
    var type: String? = null,

    @DynamoDBRangeKey
    var pseudo: String? = null,

    @DynamoDBIndexRangeKey(localSecondaryIndexName = SCORE_INDEX)
    var score: Int? = null
) {
    fun toPlayer(): Player = Player(pseudo!!, score!!)

    companion object {
        const val HIGHSCORE_TYPE = "HIGHSCORE"
        const val SCORE_INDEX = "score_index"
        fun build(player: Player) = PlayerItem(HIGHSCORE_TYPE, player.pseudo, player.score)
    }
}