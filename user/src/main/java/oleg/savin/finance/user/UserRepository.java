package oleg.savin.finance.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.stereotype.Repository;
import user.UserEntity;
@Repository
@RepositoryRestResource(path = "users")
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
