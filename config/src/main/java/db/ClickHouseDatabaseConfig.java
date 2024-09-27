package db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class ClickHouseDatabaseConfig {

    @Value("${clickhouse.datasource.url}")
    private String url;

    @Value("${clickhouse.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${clickhouse.datasource.username}")
    private String username;

    @Value("${clickhouse.datasource.password}")
    private String password;

    @Value("${clickhouse.datasource.hikari.maximum-pool-size}")
    private int maximumPoolSize;

    @Value("${clickhouse.datasource.hikari.minimum-idle}")
    private int minimumIdle;

    @Value("${clickhouse.datasource.hikari.idle-timeout}")
    private long idleTimeout;

    @Value("${clickhouse.datasource.hikari.pool-name}")
    private String poolName;

    @Bean(name = "clickHouseDataSource")
    public DataSource clickHouseDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);

        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        hikariConfig.setMinimumIdle(minimumIdle);
        hikariConfig.setIdleTimeout(idleTimeout);
        hikariConfig.setPoolName(poolName);

        return new HikariDataSource(hikariConfig);
    }
}
