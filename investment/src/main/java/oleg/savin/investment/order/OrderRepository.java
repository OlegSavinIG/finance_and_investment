package oleg.savin.investment.order;

import oleg.savin.investment.entity.OrderEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<OrderEntity, String> {
    boolean existsByIdAndOwner(String orderId, Long owner);
    boolean existsById(String orderId);

    List<OrderEntity> findByOwner(Long owner, Pageable pageable);
}
