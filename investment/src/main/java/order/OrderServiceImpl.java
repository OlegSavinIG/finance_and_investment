package order;

import checker.ExistChecker;
import lombok.RequiredArgsConstructor;
import oleg.savin.finance.user.UserRepository;
import order.searchcriteria.OrderSpecifications;
import order.searchcriteria.SortByField;
import order.searchcriteria.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    public OrderResponse updateOrder(
            Long userId, Long orderId, OrderRequestUpdate updateRequest) {
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
        Sort sortParameters = getSortParameters(sortByField, sortDirection);
        Page<OrderEntity> orderEntities = repository.findByOwnerId(
                userId, PageRequest.of(from / size, size, sortParameters)
        );
        return orderEntities.stream()
                .map(OrderMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> searchOrders(
            Long userId, OrderSearchCriteria searchCriteria,
            SortByField sortByField, SortDirection sortDirection,
            Integer from, Integer size) {
        checker.isUserExist(userId);
        Sort sortParameters = getSortParameters(sortByField, sortDirection);
        Specification<OrderEntity> specification =
                OrderSpecifications.searchByCriteria(userId, searchCriteria);
        Page<OrderEntity> entities = repository.findAll(
                specification, PageRequest.of(from / size, size, sortParameters));
        return entities.stream()
                .map(OrderMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }

    private Sort getSortParameters(
            SortByField sortByField, SortDirection sortDirection) {
        Sort.Direction direction =
                sortDirection == SortDirection.DESC ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(direction, sortByField.getFieldName());
    }

    private OrderEntity updateOrderFields(
            OrderEntity entity, OrderRequestUpdate updateRequest) {
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
