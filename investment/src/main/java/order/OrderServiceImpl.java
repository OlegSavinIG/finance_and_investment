package order;

import checker.ExistChecker;
import lombok.RequiredArgsConstructor;
import oleg.savin.finance.user.UserRepository;
import order.searchcriteria.OrderSpecifications;
import order.searchcriteria.SortByField;
import order.searchcriteria.SortDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository repository;
    private final ExistChecker checker;
    private final UserRepository userRepository;

    @Override
    public OrderResponse createOrder(Long userId, OrderRequest request) {
        logger.info("Creating a new order for user with id: {}", userId);
        checker.isUserExist(userId);
        logger.debug("User with id {} exists", userId);
        UserEntity owner = userRepository.getReferenceById(userId);
        logger.debug("Owner of the order: {}", owner.getName());
        OrderEntity entity = OrderMapper.INSTANCE.toEntity(request);
        entity.setOwner(owner);
        OrderEntity saved = repository.save(entity);
        logger.info("New order created with id: {}", saved.getId());
        return OrderMapper.INSTANCE.toResponse(saved);
    }

    @Override
    @Transactional
    public OrderResponse updateOrder
            (Long userId, Long orderId, OrderRequestUpdate updateRequest) {
        logger.info("Updating order with id: {} for user with id: {}", orderId, userId);
        checker.isUserExist(userId);
        checker.isOrderExist(orderId);
        checker.isUserOwnerOfOrder(userId, orderId);
        logger.debug("Validation checks completed for order id: {}", orderId);
        OrderEntity entity = repository.findById(orderId).orElseThrow();
        OrderEntity updatedEntity = updateOrderFields(entity, updateRequest);
        OrderEntity saved = repository.save(updatedEntity);
        logger.info("Order with id: {} successfully updated", saved.getId());
        return OrderMapper.INSTANCE.toResponse(saved);
    }

    @Override
    public List<OrderResponse> getOrders(
            Long userId, Integer from, Integer size,
            SortByField sortByField, SortDirection sortDirection) {
        logger.info("Fetching orders for user with id: {}, pagination from: {}, size: {}",
                userId, from, size);
        checker.isUserExist(userId);
        logger.debug("User with id {} exists", userId);
        Sort sortParameters = getSortParameters(sortByField, sortDirection);
        logger.info("Sort parameters {}", sortParameters);
        Page<OrderEntity> orderEntities = repository.findByOwnerId(
                userId, PageRequest.of(from, size, sortParameters));
        logger.info("Found {} orders for user with id: {}",
                orderEntities.getTotalElements(), userId);
        return orderEntities.stream()
                .map(OrderMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> searchOrders(
            Long userId, OrderSearchCriteria searchCriteria,
            SortByField sortByField, SortDirection sortDirection,
            Integer from, Integer size) {
        logger.info("Searching orders for user with id: {}, with search criteria", userId);
        checker.isUserExist(userId);
        logger.debug("User with id {} exists", userId);
        Sort sortParameters = getSortParameters(sortByField, sortDirection);
        Specification<OrderEntity> specification =
                OrderSpecifications.searchByCriteria(userId, searchCriteria);
        Page<OrderEntity> entities = repository.findAll(
                specification, PageRequest.of(from / size, size, sortParameters));
        logger.info("Found {} orders matching search criteria for user with id: {}",
                entities.getTotalElements(), userId);
        return entities.stream()
                .map(OrderMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }

    private Sort getSortParameters(
            SortByField sortByField, SortDirection sortDirection) {
        Sort.Direction direction = sortDirection == SortDirection.DESC ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        logger.debug("Applying sort by field: {}, direction: {}",
                sortByField.getFieldName(), sortDirection);
        return Sort.by(direction, sortByField.getFieldName());
    }

    private OrderEntity updateOrderFields(
            OrderEntity entity, OrderRequestUpdate updateRequest) {
        logger.debug("Updating fields of order with id: {}", entity.getId());
        if (updateRequest.getTicker() != null) {
            logger.debug("Updating ticker to: {}", updateRequest.getTicker());
            entity.setTicker(updateRequest.getTicker());
        }
        if (updateRequest.getType() != null) {
            logger.debug("Updating type to: {}", updateRequest.getType());
            entity.setType(updateRequest.getType());
        }
        if (updateRequest.getSum() != null) {
            logger.debug("Updating sum to: {}", updateRequest.getSum());
            entity.setSum(updateRequest.getSum());
        }
        if (updateRequest.getClosedTime() != null) {
            logger.debug("Updating closed time to: {}", updateRequest.getClosedTime());
            entity.setClosedTime(updateRequest.getClosedTime());
        }
        if (updateRequest.getResult() != null) {
            logger.debug("Updating result to: {}", updateRequest.getResult());
            entity.setResult(updateRequest.getResult());
        }
        return entity;
    }
}
