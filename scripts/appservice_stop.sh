kill $(ps aux | grep "server.port" | awk '{print $2}')
