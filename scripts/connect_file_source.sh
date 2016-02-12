#!/bin/bash
echo $EVA_HOME
$EVA_HOME
cd $EVA_HOME/kafka-config/
$EVA_HOME/kafka_2.11-0.9.0.0/bin/connect-standalone.sh $EVA_HOME/kafka_config/connect-standalone.properties $EVA_HOME/kafka_config/8081.properties $EVA_HOME/kafka_config/8082.properties $EVA_HOME/kafka_config/8083.properties
