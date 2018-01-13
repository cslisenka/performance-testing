#!/usr/bin/env bash
nohup java -Dserver.port=8081 -javaagent:./jmx_prometheus_javaagent-0.1.0.jar=5531:agent.yaml -jar ../target/demo-0.0.1-SNAPSHOT.jar &