package order;

import checker.ExistChecker;
import lombok.RequiredArgsConstructor;
import oleg.savin.finance.user.UserRepository;
import order.searchcriteria.SortByField;
import order.searchcriteria.SortDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
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
    private final MongoTemplate mongoTemplate;

    @Override
    public OrderResponse createOrder(Long userId, OrderRequest request) {
        checker.isUserExist(userId);
        UserEntity owner = userRepository.getReferenceById(userId);
        OrderEntity entity = OrderMapper.INSTANCE.toEntity(request);
        entity.setOwner(owner);
        OrderEntity saved = repository.save(entity);
        logger.info("Order created with id: {} for user id: {}", saved.getId(), userId);

        return OrderMapper.INSTANCE.toResponse(saved);
    }

    @Override
    public OrderResponse updateOrder(
            Long userId, String orderId, OrderRequestUpdate updateRequest) {
        checker.isUserExist(userId);
        checker.isOrderExist(orderId);
        checker.isUserOwnerOfOrder(userId, orderId);

        OrderEntity entity = repository.findById(orderId).orElseThrow();
        updateOrderFields(entity, updateRequest);
        OrderEntity saved = repository.save(entity);
        logger.info("Order updated with id: {}", saved.getId());

        return OrderMapper.INSTANCE.toResponse(saved);
    }

    @Override
    public List<OrderResponse> getOrders(
            Long userId, Integer from, Integer size,
            SortByField sortByField, SortDirection sortDirection) {
        checker.isUserExist(userId);
        PageRequest pageRequest = PageRequest.of(
                from / size, size, getSortParameters(sortByField, sortDirection));
        List<OrderEntity> orderEntities = repository.findByOwnerId(userId, pageRequest);
        logger.info("Fetched {} orders for user id: {}", orderEntities.size(), userId);

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

        Query query = new Query();
        addSearchCriteria(query, userId, searchCriteria);
        query.with(getSortParameters(sortByField, sortDirection));
        query.skip(from).limit(size);

        List<OrderEntity> entities = mongoTemplate.find(query, OrderEntity.class);
        logger.info("Found {} orders matching criteria for user id: {}", entities.size(), userId);

        return entities.stream()
                .map(OrderMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrder(String orderId) {
        mongoTemplate.remove(orderId);
    }

    private void addSearchCriteria(
            Query query, Long userId, OrderSearchCriteria searchCriteria) {

        query.addCriteria(Criteria.where("owner.id").is(userId));

        if (searchCriteria.getTicker() != null) {
            query.addCriteria(Criteria.where("ticker").is(searchCriteria.getTicker()));
        }
        if (searchCriteria.getType() != null) {
            query.addCriteria(Criteria.where("type").is(searchCriteria.getType()));
        }
        if (searchCriteria.getSumMin() != null || searchCriteria.getSumMax() != null) {
            Criteria sumCriteria = new Criteria();
            if (searchCriteria.getSumMin() != null) {
                sumCriteria.gte(searchCriteria.getSumMin());
            }
            if (searchCriteria.getSumMax() != null) {
                sumCriteria.lte(searchCriteria.getSumMax());
            }
            query.addCriteria(Criteria.where("sum").is(sumCriteria));
        }

        if (searchCriteria.getCreationTimeMin() != null || searchCriteria.getCreationTimeMax() != null) {
            Criteria creationTimeCriteria = new Criteria();
            if (searchCriteria.getCreationTimeMin() != null) {
                creationTimeCriteria.gte(searchCriteria.getCreationTimeMin());
            }
            if (searchCriteria.getCreationTimeMax() != null) {
                creationTimeCriteria.lte(searchCriteria.getCreationTimeMax());
            }
            query.addCriteria(Criteria.where("creationTime").is(creationTimeCriteria));
        }

        if (searchCriteria.getClosedTimeMin() != null || searchCriteria.getClosedTimeMax() != null) {
            Criteria closedTimeCriteria = new Criteria();
            if (searchCriteria.getClosedTimeMin() != null) {
                closedTimeCriteria.gte(searchCriteria.getClosedTimeMin());
            }
            if (searchCriteria.getClosedTimeMax() != null) {
                closedTimeCriteria.lte(searchCriteria.getClosedTimeMax());
            }
            query.addCriteria(Criteria.where("closedTime").is(closedTimeCriteria));
        }

        if (searchCriteria.getResultMin() != null || searchCriteria.getResultMax() != null) {
            Criteria resultCriteria = new Criteria();
            if (searchCriteria.getResultMin() != null) {
                resultCriteria.gte(searchCriteria.getResultMin());
            }
            if (searchCriteria.getResultMax() != null) {
                resultCriteria.lte(searchCriteria.getResultMax());
            }
            query.addCriteria(Criteria.where("result").is(resultCriteria));
        }
    }

    private Sort getSortParameters(SortByField sortByField, SortDirection sortDirection) {
        Sort.Direction direction =
                (sortDirection == SortDirection.DESC) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(direction, sortByField.getFieldName());
    }

    private void updateOrderFields(OrderEntity entity, OrderRequestUpdate updateRequest) {
        if (updateRequest.getTicker() != null) entity.setTicker(updateRequest.getTicker());
        if (updateRequest.getType() != null) entity.setType(updateRequest.getType());
        if (updateRequest.getSum() != null) entity.setSum(updateRequest.getSum());
        if (updateRequest.getClosedTime() != null) entity.setClosedTime(updateRequest.getClosedTime());
        if (updateRequest.getResult() != null) entity.setResult(updateRequest.getResult());
    }
}
