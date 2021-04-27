package scoreboard.core

import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

open class Player(
    pseudo: String, @field:Min(0) val score: Int
) {
    @field:NotEmpty
    @field:Size(min = 3, max = 50)
    val pseudo: String = pseudo.trim()

    fun updateScore(score: Int): Player = Player(pseudo, score)
}