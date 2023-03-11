# How to run the containerised database

First time use:

```bash
docker pull mongo:6.0.4-jammy
docker run --name=server-db -d -p 27017:27017 mongo:6.0.4-jammy
```

Every other time, to start or stop the docker container use:

```bash
docker start server-db
docker stop server-db
```

# Deployment

To run the server in dev mode (default) use:

```bash
mvn clean package spring-boot:run 
```

To run the server in debug mode use :

```bash
mvn clean package spring-boot:run -P debug  
```