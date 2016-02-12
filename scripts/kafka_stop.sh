kill $(ps aux | grep "kafka" | grep -v "zookeeper" | awk '{print $2}')
