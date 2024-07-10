# Hotel Room Allocation

Recruitment task for BeUsable - 8.07.2024

Author: [Mateusz Siepracki](mateusz.siepracki@gmail.com)

## Build and run the app:

1. Recommended: use all-in-one shell script `run.sh` (remember to add execution permission `chmod +x run.sh` before)
2. Execute docker build & docker run commands one by one:
   ```shell
   docker build -t hotel-room-allocation .
   docker run -p 8080:8080 --name hotel-room-allocation hotel-room-allocation
   ```
   Warning: remember to clean up the resources after running manually

## Using Swagger

After running, you can navigate to http://localhost:8080/swagger-ui/index.html and see exposed endpoints

## Running tests exclusively

There's possibility of running tests exclusively without running an application. For that, use `tests-only.sh` which is next to `run.sh`.

Test are based on Maven so if you have Maven installed and configured, running below in root catalog of project will result with the same
   ```shell
   mvn test
   ```
