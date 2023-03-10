To run the client:
First run this command to build the jar:
```bash
mvn clean package 
```

Then to run:
```bash
java --add-exports=com.google.gson/com.google.gson.internal=gson.extras -jar target/splendor-client.jar
```