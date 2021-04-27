package scoreboard.api

import javax.validation.constraints.Min

data class ScorePayload(@field:Min(0) val score: Int)