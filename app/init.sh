#!/bin/sh

AWS_ENDPOINT="$AWS_ENDPOINT_HOST:$AWS_ENDPOINT_PORT"

echo "Configuring AWS CLI"
aws configure set region us-east-1 --profile default
aws configure set aws_access_key_id $AWS_ACCESS_KEY --profile default
aws configure set aws_secret_access_key $AWS_SECRET_KEY --profile default

echo "Trying to reach aws endpoint on $AWS_ENDPOINT"
until $(curl -o /dev/null -s -f $AWS_ENDPOINT/health); do
  echo "Waiting for aws endpoint to be up..."
  sleep 2
done
echo "aws endpoint reached!"

echo "Trying to find existing db schema..."
EXISTS=$(aws --endpoint-url $AWS_ENDPOINT dynamodb list-tables | grep "players")
if [ "$?" -ne "0" ]; then
  echo "DB schema not found, creating schema"
  aws --endpoint-url $AWS_ENDPOINT dynamodb create-table --cli-input-json file://app/players_schema.json
fi

echo "Launching Scoreboard API"
sh gradlew run --args="server config.yml"
