package scoreboard.resources

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import scoreboard.api.ScorePayload
import scoreboard.api.errors.PlayerConflictException
import scoreboard.api.errors.PlayerNotFoundException
import scoreboard.core.Player
import scoreboard.core.ScoreboardService
import scoreboard.core.RankedPlayer
import java.net.URI
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


@Path(PlayerResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class PlayerResource(private val scoreboardService: ScoreboardService) {

    @GET
    @Operation(description = "Get ranked players")
    @ApiResponse(
        responseCode = "200",
        description = "Players info",
        content = [Content(array = ArraySchema(schema = Schema(implementation = RankedPlayer::class)))]
    )
    fun getAll(): List<RankedPlayer> = scoreboardService.getAllPlayerRanks()

    @Path("/{pseudo}")
    @GET
    @Operation(
        description = "Get one player info", responses = [
            ApiResponse(
                responseCode = "200",
                description = "Player info",
                content = [Content(schema = Schema(implementation = RankedPlayer::class))]
            ),
            ApiResponse(responseCode = "404", description = "Player not found")
        ]
    )
    fun getOne(@PathParam("pseudo") pseudo: String): RankedPlayer =
        scoreboardService.getPlayerRank(pseudo) ?: throw PlayerNotFoundException(pseudo)

    @POST
    @Operation(
        description = "Create a player", responses = [
            ApiResponse(
                responseCode = "201",
                description = "Created player",
                content = [Content(schema = Schema(implementation = Player::class))]
            ),
            ApiResponse(responseCode = "409", description = "Player already exists")
        ]
    )
    fun create(@NotNull @Valid player: Player): Response {
        val existingPlayer = scoreboardService.getPlayer(player.pseudo)
        existingPlayer?.let {
            throw PlayerConflictException(it.pseudo)
        }
        val created = scoreboardService.savePlayer(player)
        return Response.created(URI("players/${created.pseudo}")).entity(created).build()
    }

    @Path("/{pseudo}")
    @PATCH
    @Operation(
        description = "Update player score", responses = [
            ApiResponse(
                responseCode = "200",
                description = "Updated player info",
                content = [Content(schema = Schema(implementation = Player::class))]
            ),
            ApiResponse(responseCode = "404", description = "Player not found")
        ]
    )
    fun setScore(@PathParam("pseudo") pseudo: String, @NotNull @Valid scoreUpdate: ScorePayload): Player =
        when (val playerFound = scoreboardService.getPlayer(pseudo)) {
            null -> throw PlayerNotFoundException(pseudo)
            else -> scoreboardService.savePlayer(playerFound.updateScore(scoreUpdate.score))
        }

    @DELETE
    @ApiResponse(
        responseCode = "200",
        description = "Final scoreboard before deletion",
        content = [Content(array = ArraySchema(schema = Schema(implementation = RankedPlayer::class)))]
    )
    fun closeScoreboard(): List<RankedPlayer> = scoreboardService.emptyScoreboard()

    companion object {
        const val PATH = "/players"
    }
}