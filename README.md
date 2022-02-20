# Network Performace Testing

## Server Setup 

(1) Select which packet protocol (TCP, UDP) you want the server to accept
``` 
./gradlew TCP_server
./gradlew UDP_server
```

## Client Setup
(1) Allow the ```startBenchmark.sh``` to be an executable

(2) Select which packet protocol (TCP, UDP) you want the client to send (MAKE SURE THE SERVER AND CLIENT ARE USING THE SAME PROTOCOL)

(3) Set the server host IP in ```netconf.txt``` for the client to know where to send packets. 
```
./startBenchmark.sh TCP
./startBenchmark.sh UDP
```
