package oleg.savin.investment.mongoconfig;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "oleg.savin.investment.order")
public class MongoDatabaseConfig extends AbstractMongoClientConfiguration {

    @Override
    protected @NotNull String getDatabaseName() {
        return "investment-db";
    }

    @Bean
    public @NotNull MongoClient mongoClient() {
        return MongoClients.create("mongodb://mongo-db:27017");
    }
}
