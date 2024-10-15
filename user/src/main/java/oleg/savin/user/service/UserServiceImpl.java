package oleg.savin.user.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import oleg.savin.user.entity.UserEntity;
import oleg.savin.user.entity.UserMapper;
import oleg.savin.user_dto.UserResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserResponse findUserById(Long id) {
        UserEntity userEntity = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        return UserMapper.INSTANCE.toResponse(userEntity);
    }

    @Override
    public boolean userExists(Long id) {
        return repository.existsById(id);
    }
}
