package oleg.savin.investment.order.feign;

import oleg.savin.config.feign.FeignConfiguration;
import oleg.savin.user_dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", configuration = FeignConfiguration.class)
public interface UserClient {

    @GetMapping("/users/{id}")
    UserResponse getUserById(@PathVariable("id") Long id);
    @GetMapping("/users/exists/{id}")
    boolean existsById(@PathVariable("id") Long userId);
}
