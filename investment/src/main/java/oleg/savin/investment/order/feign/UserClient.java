package oleg.savin.investment.order.feign;

import oleg.savin.user_dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/feign/{id}")
    UserResponse getUserById(@PathVariable("id") Long id);
    @GetMapping("/feign/exists/{id}")
    boolean existsById(@PathVariable("id") Long userId);
}
