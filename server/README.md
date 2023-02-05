# How to run the containerised database

First time use:
```
docker pull mongo:6.0.4-jammy
docker run --name=server-db -d -p 27017:27017 mongo:6.0.4-jammy
```

Every other time, to start or stop the docker container use:
```
docker start server-db
docker stop server-db
```