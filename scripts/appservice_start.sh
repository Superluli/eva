#!/bin/bash
echo "building jar..."
cd $EVA_HOME/appservice/
mvn package
echo "running services..."

partition=0
for i in {8081..8083}
do

	echo $partition
	echo "staring server on $i"
	nohup java -jar $EVA_HOME/appservice/target/appservice-1.0.0.jar --server.port=$i --app.logging.kafka.partition=$partition -Xms512m -Xmx512m > $EVA_HOME/logs/app_services_$i.out &
	((partition ++))

done

cd $EVA_HOME
