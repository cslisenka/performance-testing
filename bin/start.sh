rm logs/*
nohup java -Dserver.port=8081 -javaagent:./jmx_prometheus_javaagent-0.1.0.jar=5531:agent.yaml -server -XX:+PrintFlagsFinal -Xmx5g -XX:+UseG1GC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCCause -XX:+PrintTenuringDistribution -verbose:gc -Xloggc:logs/gc.log -jar ../target/demo-0.0.1-SNAPSHOT.jar &
ps -aux | grep java
