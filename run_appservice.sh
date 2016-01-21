#!/bin/bash
echo "stopping services..."
kill $(ps aux | grep "server.port" | awk '{print $2}')
echo "building jar..."
cd /Users/Lu/workspace/eva/appservice/
mvn package
echo "running services..."
cd ../

for i in {8081..8085}; 
do
	echo "staring server on $i"
	nohup java -jar appservice/target/appservice-0.0.1-SNAPSHOT.jar --server.port=$i -Xms512m -Xmx512m > logs/app_services.out &
done
