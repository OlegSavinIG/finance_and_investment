package oleg.savin.user.postgresconfig;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostgresFlywayConfig {

    @Bean
    public Flyway flywayPostgres() {
        return Flyway.configure()
                .dataSource("jdbc:postgresql://postgres-db:5432/main_db", "postgres", "main_password")
                .locations("classpath:db/migration/postgres")  // Путь для миграций PostgreSQL
                .load();
    }
}
