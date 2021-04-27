#!/usr/bin/env sh

if [ -f .env ]; then
  export $(cat .env | xargs)
else
  echo ".env file not found, you can create one using .env.example"
  exit 1
fi
./gradlew run --args="server config.yml"