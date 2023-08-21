#!/bin/bash

# Define variables
CONTAINER_NAME="qe-spring-postgres"
POSTGRES_DB="postgres_spring_qe"
POSTGRES_PASSWORD="password"
POSTGRES_USER="qa"
HOST_PORT="9011"
DOCKER_IMAGE="postgres:15.2-alpine"

# Check if the container is already running
if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
    echo "Container $CONTAINER_NAME is already running."
    exit 0
fi

# Check if the container exists but is not running
if [ "$(docker ps -aq -f status=exited -f name=$CONTAINER_NAME)" ]; then
    # Remove the existing container
    docker rm $CONTAINER_NAME
fi

# Run the PostgreSQL container
docker run --restart unless-stopped -d --name $CONTAINER_NAME -e POSTGRES_USER=$POSTGRES_USER \
    -e POSTGRES_PASSWORD=$POSTGRES_PASSWORD -e POSTGRES_DB=$POSTGRES_DB \
    -p $HOST_PORT:5432 $DOCKER_IMAGE

# Check if the container started successfully
if [ $? -eq 0 ]; then
    echo "Container $CONTAINER_NAME is running."
else
    echo "Failed to start container $CONTAINER_NAME."
    echo "Failed to start container $CONTAINER_NAME."
fi