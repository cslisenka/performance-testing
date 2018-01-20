package com.example.demo;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DBConfiguration {

    @Autowired
    @Bean
    public JdbcTemplate jdbc(DataSource hikariDs) {
        return new JdbcTemplate(hikariDs);
    }

    @Bean
    public DataSource hikariDs(
            @Value("${db.max.pool.size:10}") int maxPoolSize,
            @Value("${db.host:localhost}") String host,
            @Value("${db.schema:performance}") String schema,
            @Value("${db.username:root}") String username,
            @Value("${db.password:root}") String password) {

        MysqlDataSource ds = new MysqlDataSource();
        ds.setAutoReconnect(true);
        ds.setCreateDatabaseIfNotExist(true);
        ds.setServerName(host);
        ds.setDatabaseName(schema);
        ds.setUser(username);
        ds.setPassword(password);

        HikariConfig config = new HikariConfig();
        config.setDataSource(ds);
        config.setConnectionTimeout(10_000);
        config.setMaximumPoolSize(maxPoolSize);
        config.setPoolName("hikari");
        return new HikariDataSource(config);
    }
}
