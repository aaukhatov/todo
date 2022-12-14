# TODO

## Requirements

- Java 17
- Docker or Standalone Mongo DB
- Google API Keys for OAuth2

## Build

Go to the project directory
```shell
./gradlew build
```

## Run Mongo DB

```shell
docker run -it --rm -p 27017:27017 mongo
```

## Run TODO

In order to authorize we need Google API Keys.

> If you're going to use your `client_id`, please be sure to set up these redirects: http://localhost:8080/oauth2/authorization/google, http://localhost:8080/login/oauth2/code/google

```shell
env GOOGLE_CLIENT_ID=<value> GOOGLE_CLIENT_SECRET=<value> java -jar build/libs/todo-0.0.1-SNAPSHOT.jar
```

## Acquire JWT token

Open your favorite web browser and follow the link http://localhost:8080/login

The response is a JWT token, it requires to get access.

## Import Postman Collection

In the project root directory you can find Postman collection, you export it

## Examples

### Create

```
POST /api/v1/tasks
Content-Type: application/json
Authorization: Bearer <token>

{
    "title": "Task",
    "description": "Hello, world!"
}
```

### Get by id

```
GET /api/v1/tasks/{taskId}
Content-Type: application/json
Authorization: Bearer <token>
```

### Get all my tasks

```
GET /api/v1/tasks
Content-Type: application/json
Authorization: Bearer <token>
```

### Edit

```
PUT /api/v1/tasks/{taskId}
Content-Type: application/json
Authorization: Bearer <token>

{
    "title": "Task",
    "description": "Hello, Australia!"
}
```

### Delete

```
DELETE /api/v1/tasks/{taskId}
Content-Type: application/json
Authorization: Bearer <token>
```
