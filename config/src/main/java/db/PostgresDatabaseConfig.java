package db;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.main.repository", // Укажите пакет для репозиториев основной базы
        entityManagerFactoryRef = "mainEntityManagerFactory",
        transactionManagerRef = "mainTransactionManager"
)
public class PostgresDatabaseConfig {

    @Bean(name = "mainDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource mainDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mainEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mainEntityManagerFactory(
            DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.example.main.entity"); // Укажите пакет для сущностей
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return em;
    }

    @Bean(name = "mainTransactionManager")
    public JpaTransactionManager mainTransactionManager(
            LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory.getObject());
    }
}

