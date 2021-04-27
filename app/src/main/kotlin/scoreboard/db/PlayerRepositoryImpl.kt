package scoreboard.db

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import scoreboard.ScoreboardConfiguration
import scoreboard.core.Player
import scoreboard.core.PlayerRepository

class PlayerRepositoryImpl(clientProvider: DynamoDbClientProvider) : PlayerRepository {
    private val mapper = DynamoDBMapper(clientProvider.client)

    override fun findAllSortedByBestScore(): List<Player> {
        val queryExpression = queryAllExpression()
            .withIndexName(PlayerItem.SCORE_INDEX)
            .withScanIndexForward(false)
        return mapper
            .query(PlayerItem::class.java, queryExpression)
            .toList()
            .map { it.toPlayer() }
    }

    private fun queryAllExpression(): DynamoDBQueryExpression<PlayerItem> {
        val attributeValues = mapOf(":hashKey" to AttributeValue().withS(PlayerItem.HIGHSCORE_TYPE))
        return DynamoDBQueryExpression<PlayerItem>()
            .withKeyConditionExpression("#type = :hashKey")
            .withExpressionAttributeNames(mapOf("#type" to "type"))
            .withExpressionAttributeValues(attributeValues)
    }

    override fun findBy(pseudo: String): Player? =
        mapper
            .load(PlayerItem::class.java, PlayerItem.HIGHSCORE_TYPE, pseudo)
            ?.toPlayer()

    override fun upsert(player: Player): Player {
        mapper.save(PlayerItem.build(player))
        return player
    }

    override fun deleteAll() {
        val allPlayers = mapper.query(PlayerItem::class.java, queryAllExpression())
        mapper.batchDelete(allPlayers)
    }
}