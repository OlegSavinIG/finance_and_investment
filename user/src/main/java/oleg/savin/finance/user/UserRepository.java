package oleg.savin.finance.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.stereotype.Repository;
import user.UserEntity;

@RepositoryRestController
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
