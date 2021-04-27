# Scoreboard

A scoreboard service to rank players.

## Installation

Clone the project

```bash
git clone https://github.com/brigondaud/scoreboard
```

Go to the project directory

```bash
cd scoreboard
```

Create a `.env` file by copying the `.env.example`. It contains the environment variables used to deploy the service
locally or run the service locally.

```bash
cp .env.example .env
```

## Deploy locally

To deploy a local stack containing the Scoreboard API you will need [Docker](https://www.docker.com/)
with `docker-compose`
setup on your system. To deploy the stack:

```bash
./deploy-local.sh
```

*Notes:* The local `app` module is used to build then deploy the service, thus the stack may take some time to be up and
running.

The service will be available dy default on http://localhost:8080.

## API Reference

When a local instance of the service is running, you may find the Swagger specification at one of the following
endpoints:

- http://localhost:8080/openapi.json
- http://localhost:8080/openapi.yaml

## Run locally

*Notes:* This step requires to have a configured and running DynamoDB service along with a `.env` file correctly
configured. It also requires to have java 8 setup on your system.

You may run the service alone, to do so run:

```bash
./start.sh
```

## Running Tests

To run tests, run the following command

```bash
./gradlew test
```