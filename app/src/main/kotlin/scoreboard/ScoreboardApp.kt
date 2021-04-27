package scoreboard

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.dropwizard.Application
import io.dropwizard.configuration.EnvironmentVariableSubstitutor
import io.dropwizard.configuration.SubstitutingSourceProvider
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import scoreboard.core.ScoreboardServiceImpl
import scoreboard.db.DynamoDbClientProvider
import scoreboard.db.PlayerRepositoryImpl
import scoreboard.resources.OpenApiResourceProvider
import scoreboard.resources.PlayerResource


class ScoreboardApplication : Application<ScoreboardConfiguration>() {
    override fun run(configuration: ScoreboardConfiguration, environment: Environment) {
        environment.jersey()
            .register(PlayerResource(ScoreboardServiceImpl(PlayerRepositoryImpl(DynamoDbClientProvider(configuration)))))
        environment.jersey()
            .register(OpenApiResourceProvider(configuration).resource)
    }

    override fun initialize(bootstrap: Bootstrap<ScoreboardConfiguration>?) {
        bootstrap?.objectMapper
            ?.registerModule(KotlinModule())
            ?.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true)
            ?.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
        bootstrap?.configurationSourceProvider = SubstitutingSourceProvider(
            bootstrap?.configurationSourceProvider,
            EnvironmentVariableSubstitutor()
        )
    }
}

fun main(args: Array<String>) = ScoreboardApplication().run(*args)