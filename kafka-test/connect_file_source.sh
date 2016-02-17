#!/bin/bash
cd $EVA_HOME/kafka-test/
$EVA_HOME/kafka_2.11-0.9.0.0/bin/connect-standalone.sh connect-standalone.properties 8081.properties 8082.properties 8083.properties
