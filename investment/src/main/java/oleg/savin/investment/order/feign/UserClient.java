package oleg.savin.investment.order.feign;

import oleg.savin.models_dto.user.UserEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/users/{id}")
    UserEntity getUserById(@PathVariable("id") Long id);
    @GetMapping("/users/exists/{id}")
    boolean existsById(@PathVariable("id") Long userId);
}
