package oleg.savin.user.service;

import oleg.savin.user_dto.UserResponse;

public interface UserService {
    UserResponse findUserById(Long id);

    boolean userExists(Long id);
}
