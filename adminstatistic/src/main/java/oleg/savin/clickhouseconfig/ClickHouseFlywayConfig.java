//package oleg.savin.statistics.clickhouseconfig;
//
//import org.flywaydb.core.Flyway;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class ClickHouseFlywayConfig {
//
//    @Bean
//    public Flyway flywayClickHouse() {
//        return Flyway.configure()
//                .dataSource("jdbc:clickhouse://clickhouse-db:8123/default", "default", "")
//                .locations("classpath:db/migration/clickhouse")  // Путь для миграций ClickHouse
//                .load();
//    }
//}
