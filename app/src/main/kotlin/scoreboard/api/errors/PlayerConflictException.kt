package scoreboard.api.errors

import javax.ws.rs.WebApplicationException

class PlayerConflictException(pseudo: String) : WebApplicationException("Player $pseudo already exists", 409)