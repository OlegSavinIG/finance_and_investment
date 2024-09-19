package order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    boolean existsByIdAndOwnerId(Long orderId, Long ownerId);

    Page<OrderEntity> findByOwnerId(Long userId, PageRequest of);
}
