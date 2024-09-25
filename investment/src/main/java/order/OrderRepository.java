package order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<OrderEntity, String>, JpaSpecificationExecutor<OrderEntity> {
    boolean existsByIdAndOwnerId(String orderId, Long ownerId);
    boolean existsById(String orderId);

    List<OrderEntity> findByOwnerId(Long userId, Pageable pageable);
    List<OrderEntity> searchByCriteria(
            String ownerId, OrderSearchCriteria searchCriteria, PageRequest pageRequest);
}
