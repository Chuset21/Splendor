# Running the client

First run this command to build the jar:
```bash
mvn clean package 
```

Then to run:
```bash
java --add-exports=com.google.gson/com.google.gson.internal=gson.extras -jar target/splendor-client.jar
```

If you would like to give the IP of lobby service through command line arguments (127.0.0.1 by default):
```bash
java --add-exports=com.google.gson/com.google.gson.internal=gson.extras -jar target/splendor-client.jar <lobby-service-ip>
```

Be aware that warnings about shading will occur and can be safely ignored when compiling.  
Warnings about filename-based automodules will also occur, but this is fine since we don't  want to publish this project to a public artifact repository.