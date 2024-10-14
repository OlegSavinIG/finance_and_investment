package oleg.savin.investment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "oleg.savin.config")
@EnableFeignClients
@EnableEurekaClient
public class InvestmentApplication {
    public static void main(String[] args) {
        SpringApplication.run(InvestmentApplication.class, args);
    }
}
