package scoreboard.resources

import io.swagger.v3.jaxrs2.integration.resources.BaseOpenApiResource
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource
import io.swagger.v3.oas.integration.SwaggerConfiguration
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import scoreboard.ScoreboardConfiguration

class OpenApiResourceProvider(configuration: ScoreboardConfiguration) {
    val resource: BaseOpenApiResource =
        OpenApiResource()
            .openApiConfiguration(
                SwaggerConfiguration()
                    .openAPI(
                        OpenAPI().info(
                            Info()
                                .title("Scoreboard service API")
                                .description("A simple scoreboard service")
                                .version(configuration.version)
                        )
                    )
                    .prettyPrint(true)
                    .resourcePackages(
                        setOf("scoreboard.resources")
                    )
            )
}