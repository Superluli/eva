kill $(ps aux | grep "mongod" | awk '{print $2}')
