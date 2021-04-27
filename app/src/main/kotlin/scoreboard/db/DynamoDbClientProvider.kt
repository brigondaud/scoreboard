package scoreboard.db

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import scoreboard.ScoreboardConfiguration

class DynamoDbClientProvider(config: ScoreboardConfiguration) {
    val client: AmazonDynamoDB = AmazonDynamoDBClientBuilder
        .standard()
        .withEndpointConfiguration(
            AwsClientBuilder.EndpointConfiguration(config.awsEndpoint, config.awsRegion)
        )
        .withCredentials(
            AWSStaticCredentialsProvider(BasicAWSCredentials(config.awsAccessKey, config.awsSecretKey))
        )
        .build()
}