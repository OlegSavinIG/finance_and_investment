package order;

import checker.ExistChecker;
import lombok.RequiredArgsConstructor;
import oleg.savin.finance.user.UserRepository;
import order.searchcriteria.SortByField;
import order.searchcriteria.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository repository;
    private final ExistChecker checker;
    private final UserRepository userRepository;

    @Override
    public OrderResponse createOrder(Long userId, OrderRequest request) {
        checker.isUserExist(userId);
        UserEntity owner = userRepository.getReferenceById(userId);
        OrderEntity entity = OrderMapper.INSTANCE.toEntity(request);
        entity.setOwner(owner);
        OrderEntity saved = repository.save(entity);
        return OrderMapper.INSTANCE.toResponse(saved);
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long userId, Long orderId, OrderRequestUpdate updateRequest) {
        checker.isUserExist(userId);
        checker.isOrderExist(orderId);
        checker.isUserOwnerOfOrder(userId, orderId);
        OrderEntity entity = repository.findById(orderId).get();
        OrderEntity updatedEntity = updateOrderFields(entity, updateRequest);
        OrderEntity saved = repository.save(updatedEntity);
        return OrderMapper.INSTANCE.toResponse(saved);
    }

    @Override
    public List<OrderResponse> getOrders(
            Long userId, Integer from, Integer size,
            SortByField sortByField, SortDirection sortDirection) {
        checker.isUserExist(userId);
        Sort.Direction direction = sortDirection == SortDirection.DESC ? Sort.Direction.DESC : Sort.Direction.ASC;
        Page<OrderEntity> orderEntities = repository.findByOwnerId(
                userId, PageRequest.of(from/size, size, Sort.by(direction, sortByField.getFieldName()))
        );
        return orderEntities.stream()
                .map(OrderMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> searchOrders(Long userId, OrderSearchCriteria searchCriteria) {
        return null;
    }

    private OrderEntity updateOrderFields(OrderEntity entity, OrderRequestUpdate updateRequest) {
        if (updateRequest.getTicker() != null) {
            entity.setTicker(updateRequest.getTicker());
        }
        if (updateRequest.getType() != null) {
            entity.setType(updateRequest.getType());
        }
        if (updateRequest.getSum() != null) {
            entity.setSum(updateRequest.getSum());
        }
        if (updateRequest.getClosedTime() != null) {
            entity.setClosedTime(updateRequest.getClosedTime());
        }
        if (updateRequest.getResult() != null) {
            entity.setResult(updateRequest.getResult());
        }
        return entity;
    }
}
