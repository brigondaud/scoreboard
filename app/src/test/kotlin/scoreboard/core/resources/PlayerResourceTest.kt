package scoreboard.core.resources

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput
import org.junit.*
import scoreboard.ScoreboardConfiguration
import scoreboard.api.ScorePayload
import scoreboard.api.errors.PlayerConflictException
import scoreboard.api.errors.PlayerNotFoundException
import scoreboard.core.Player
import scoreboard.core.ScoreboardServiceImpl
import scoreboard.db.DynamoDbClientProvider
import scoreboard.db.PlayerItem
import scoreboard.db.PlayerRepositoryImpl
import scoreboard.resources.PlayerResource
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class PlayerResourceTest {
    companion object {
        private val config = ScoreboardConfiguration.testConfig
        private val clientProvider = DynamoDbClientProvider(config)
        private val playerResource =
            PlayerResource(ScoreboardServiceImpl(PlayerRepositoryImpl(clientProvider)))
        private lateinit var localDynamoDbServer: DynamoDBProxyServer

        @BeforeClass
        @JvmStatic
        fun beforeAll() {
            localDynamoDbServer = ServerRunner.createServerFromCommandLineArgs(
                arrayOf("-inMemory", "-port", ScoreboardConfiguration.testConfig.awsEndpointPort)
            )
            localDynamoDbServer.start()
        }

        @AfterClass
        @JvmStatic
        fun afterAll() {
            localDynamoDbServer.stop()
        }
    }

    @Before
    fun before() {
        val client = clientProvider.client
        val mapper = DynamoDBMapper(client)
        val tableRequest = mapper.generateCreateTableRequest(PlayerItem::class.java)
        tableRequest.provisionedThroughput = ProvisionedThroughput(20L, 10L)
        client.createTable(tableRequest)
    }

    @After
    fun after() {
        clientProvider.client.deleteTable("players")
    }

    @Test
    fun `should fail with player not found when fetching an unknown player`() {
        assertFailsWith<PlayerNotFoundException> {
            playerResource.getOne("notFound")
        }
    }

    @Test
    fun `should fail with player not found when updating an unknown player`() {
        assertFailsWith<PlayerNotFoundException> {
            playerResource.setScore("notFound", ScorePayload(0))
        }
    }

    @Test
    fun `should return an empty scoreboard when no player exist`() {
        assertEquals(listOf(), playerResource.getAll())
    }

    @Test
    fun `should return an empty scoreboard when emptying a scoreboard with no players`() {
        assertEquals(listOf(), playerResource.closeScoreboard())
    }

    @Test
    fun `should return a created response when adding a not already existing player`() {
        val newPlayer = Player("johnDoe", 0)
        val response = playerResource.create(newPlayer)
        assertEquals(201, response.status)
        assertEquals(newPlayer, response.entity)
        assertNotNull(response.location)
    }

    @Test
    fun `should trim pseudo when adding a new player`() {
        val response = playerResource.create(Player("    spaced  ", 0))
        assertEquals("spaced", (response.entity as Player).pseudo)
    }

    @Test
    fun `should fail with player conflict when creating a player with the same pseudo exists`() {
        assertFailsWith<PlayerConflictException> {
            playerResource.create(Player("same", 0))
            playerResource.create(Player("same", 10))
        }
    }

    @Test
    fun `should allow to fetch an existing player information`() {
        val player = Player("janeDoe", 200)
        playerResource.create(player)
        val playerFound = playerResource.getOne(player.pseudo)
        assertEquals(player.pseudo, playerFound.pseudo)
        assertEquals(player.score, playerFound.score)
    }

    @Test
    fun `should be able to return the scoreboard with every added players`() {
        val first = Player("janeDoe", 200)
        val second = Player("johnDoe", 100)
        playerResource.create(first)
        playerResource.create(second)
        val scoreboard = playerResource.getAll()
        assertEquals(listOf(first, second).map { it.pseudo }, scoreboard.map { it.pseudo })
        assertEquals(listOf(1, 2), scoreboard.map { it.rank })
    }

    @Test
    fun `should allow to update an existing player's score`() {
        val player = Player("one", 200)
        playerResource.create(player)
        val scoreUpdate = ScorePayload(300)
        val updatedPlayer = playerResource.setScore(player.pseudo, scoreUpdate)
        assertEquals(scoreUpdate.score, updatedPlayer.score)
    }

    @Test
    fun `should return all players when emptying the scoreboard`() {
        val first = Player("janeDoe", 200)
        val second = Player("johnDoe", 100)
        playerResource.create(first)
        playerResource.create(second)
        val scoreboard = playerResource.closeScoreboard()
        assertEquals(listOf(first, second).map { it.pseudo }, scoreboard.map { it.pseudo })
        val emptyScoreboard = playerResource.getAll()
        assertEquals(listOf(), emptyScoreboard)
    }
}