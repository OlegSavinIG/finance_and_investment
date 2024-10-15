package oleg.savin.user.service;

import lombok.RequiredArgsConstructor;
import oleg.savin.user.entity.UserEntity;
import oleg.savin.user_dto.UserResponse;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        UserResponse user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> userExists(@PathVariable("id") Long id) {
        boolean exists = userService.userExists(id);
        return ResponseEntity.ok(exists);
    }
}
