#!/bin/bash
echo "building jar..."
cd $EVA_HOME/appservice/
mvn package
echo "running services..."

for i in {8081..8083}
do
	echo "staring server on $i"
	nohup java -jar $EVA_HOME/appservice/target/appservice-1.0.0.jar --server.port=$i -Xms512m -Xmx512m > $EVA_HOME/logs/app_services_$i.out &
done

cd $EVA_HOME
