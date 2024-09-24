package db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;

@Configuration
public class ClickHouseDatabaseConfig {

    @Bean(name = "clickHouseDataSource")
    public DataSource clickHouseDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:clickhouse://localhost:8123/default")
                .driverClassName("ru.yandex.clickhouse.ClickHouseDriver")
                .build();
    }
}

