package oleg.savin.investment.order;

import lombok.RequiredArgsConstructor;
import oleg.savin.investment.checker.ExistChecker;
import oleg.savin.investment.entity.OrderEntity;
import oleg.savin.investment.entity.OrderMapper;
import oleg.savin.investment.order.searchcriteria.SortByField;
import oleg.savin.investment.order.searchcriteria.SortDirection;
import oleg.savin.order_dto.OrderRequest;
import oleg.savin.order_dto.OrderRequestUpdate;
import oleg.savin.order_dto.OrderResponse;
import oleg.savin.order_dto.OrderSearchCriteria;
import oleg.savin.order_dto.OrderStatus;
import oleg.savin.statistic_dto.StatisticRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository repository;
    private final ExistChecker checker;
    private final MongoTemplate mongoTemplate;
    private final StatisticSenderService statisticSenderService;
    private final AsyncOrderService asyncOrderService;

    @Override
    public OrderResponse createOrder(Long userId, OrderRequest request) {
        checker.isUserExist(userId);
        OrderEntity entity = OrderMapper.INSTANCE.toEntity(request);
        entity.setOwner(userId);
        entity.setOrderStatus(OrderStatus.OPEN);
        entity.setProgramCreationTime(LocalDateTime.now());
        OrderEntity saved = repository.save(entity);
        logger.info("Order created with id: {} for user id: {}", saved.getId(), userId);

        return OrderMapper.INSTANCE.toResponse(saved);
    }

    @Override
    public CompletableFuture<OrderResponse> createOrderAsync(Long userId, OrderRequest request) {
        checker.isUserExist(userId);
        return asyncOrderService.createOrderAsync(request);
    }


    @Override
    @Transactional
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
        List<OrderEntity> orderEntities = repository.findByOwner(userId, pageRequest);
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
    @Transactional
    public void closeOrder(Long userId, String orderId, OrderRequestUpdate requestUpdate) {
        checker.isUserExist(userId);
        checker.isOrderExist(orderId);
        checker.isUserOwnerOfOrder(userId, orderId);

        OrderEntity entity = repository.findById(orderId).orElseThrow();
        updateOrderFields(entity, requestUpdate);
        entity.setOrderStatus(OrderStatus.CLOSED);
        statisticSenderService.sendStatistic(createStatistic(entity));
        repository.save(entity);
        logger.info("Order with ID: {} closed", entity.getId());
    }

    private StatisticRequest createStatistic(OrderEntity entity) {
        logger.info("Saving statistic for order ID: {}", entity.getId());
        return StatisticRequest.builder()
                .closedTime(entity.getClosedTime())
                .creationTime(entity.getCreationTime())
                .type(entity.getType())
                .result(entity.getResult())
                .ticker(entity.getTicker())
                .programCreationTime(entity.getProgramCreationTime())
                .userId(entity.getOwner())
                .build();
    }

    @Override
    public void deleteOrder(String orderId) {
        Query query = new Query(Criteria.where("_id").is(orderId));
        mongoTemplate.remove(query, OrderEntity.class);
    }


    private void addSearchCriteria(Query query, Long userId, OrderSearchCriteria searchCriteria) {
        List<Criteria> criteriaList = new ArrayList<>();

        criteriaList.add(Criteria.where("owner").is(userId));

        Optional.ofNullable(searchCriteria.getTicker())
                .ifPresent(ticker -> criteriaList.add(Criteria.where("ticker").is(ticker)));

        Optional.ofNullable(searchCriteria.getType())
                .ifPresent(type -> criteriaList.add(Criteria.where("type").is(type)));

        if (searchCriteria.getSumMin() != null || searchCriteria.getSumMax() != null) {
            Criteria sumCriteria = Criteria.where("sum");
            if (searchCriteria.getSumMin() != null)
                sumCriteria = sumCriteria.gte(searchCriteria.getSumMin());
            if (searchCriteria.getSumMax() != null)
                sumCriteria = sumCriteria.lte(searchCriteria.getSumMax());
            criteriaList.add(sumCriteria);
        }

        if (searchCriteria.getCreationTimeMin() != null || searchCriteria.getCreationTimeMax() != null) {
            Criteria creationTimeCriteria = Criteria.where("creationTime");
            if (searchCriteria.getCreationTimeMin() != null)
                creationTimeCriteria = creationTimeCriteria.gte(searchCriteria.getCreationTimeMin());
            if (searchCriteria.getCreationTimeMax() != null)
                creationTimeCriteria = creationTimeCriteria.lte(searchCriteria.getCreationTimeMax());
            criteriaList.add(creationTimeCriteria);
        }

        query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
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
