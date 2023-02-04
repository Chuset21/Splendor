# How to run the containerised database
```
 docker build -t "server-db:Dockerfile" . -f Dockerfile-server-db
 docker run --platform linux/amd64 --name=server-db -p 27017:27017 -d server-db:Dockerfile 
```