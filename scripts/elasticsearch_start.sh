kill $(ps aux | grep "elasticsearch" | awk '{print $2}')
$EVA_HOME/elk/elasticsearch-2.1.1/bin/elasticsearch -d -Des.cluster.name=elasticsearch -Des.node.name=eva_node1
$EVA_HOME/elk/elasticsearch-2.1.1/bin/elasticsearch -d -Des.cluster.name=elasticsearch -Des.node.name=eva_node2
