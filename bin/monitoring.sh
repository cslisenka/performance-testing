nohup sudo node_exporter/node_exporter --web.listen-address=":9100" &
nohup mysqld_exporter/mysqld_exporter -config.my-cnf="mysqld_exporter/config" --web.listen-address=":9104" &
