package scoreboard

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration
import javax.validation.constraints.NotEmpty

data class ScoreboardConfiguration(
    @NotEmpty
    @JsonProperty
    var version: String,

    @NotEmpty
    @JsonProperty
    var awsEndpointHost: String,

    @NotEmpty
    @JsonProperty
    var awsEndpointPort: String,

    @NotEmpty
    @JsonProperty
    var awsRegion: String,

    @NotEmpty
    @JsonProperty
    var awsAccessKey: String,

    @NotEmpty
    @JsonProperty
    var awsSecretKey: String,
) : Configuration() {

    val awsEndpoint = "$awsEndpointHost:$awsEndpointPort"

    companion object {
        val testConfig = ScoreboardConfiguration(
            "1.0.0",
            "http://localhost",
            "8000",
            "us-east-1",
            "dummy",
            "dummy"
        )

    }
}