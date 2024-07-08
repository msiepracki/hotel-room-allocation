#!/usr/bin/env sh

cleanup() {
  echo "Stopping and removing the container..."
  docker stop hotel-room-allocation
  docker rm hotel-room-allocation
  exit
}

docker build -t hotel-room-allocation .
docker run -p 8080:8080 --name hotel-room-allocation hotel-room-allocation &

DOCKER_PID=$!
trap cleanup INT
wait $DOCKER_PID
