#!/usr/bin/env sh

cleanup() {
  echo "Stopping and removing the container..."
  docker stop hotel-room-allocation-test
  docker rm hotel-room-allocation-test
  exit
}

docker build --target test -t hotel-room-allocation-test .
docker run --name hotel-room-allocation-test hotel-room-allocation-test &

CONTAINER_ID=$(docker ps -q --filter "name=hotel-room-allocation-test")
trap cleanup INT
docker wait $CONTAINER_ID

docker cp hotel-room-allocation-test:/app/target/surefire-reports ./surefire-reports

cleanup