package scoreboard.api.errors

import javax.ws.rs.NotFoundException

class PlayerNotFoundException(pseudo: String) : NotFoundException("Player $pseudo not found")