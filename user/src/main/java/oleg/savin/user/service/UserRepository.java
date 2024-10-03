package oleg.savin.user.service;

import oleg.savin.models_dto.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(path = "users")
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
