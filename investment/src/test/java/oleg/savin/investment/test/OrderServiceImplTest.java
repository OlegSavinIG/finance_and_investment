package oleg.savin.investment.test;

import oleg.savin.investment.checker.ExistChecker;
import oleg.savin.investment.order.OrderRepository;
import oleg.savin.investment.order.OrderServiceImpl;
import oleg.savin.models_dto.order.*;
import oleg.savin.models_dto.statistic.StatisticRequest;
import oleg.savin.investment.order.searchcriteria.SortByField;
import oleg.savin.investment.order.searchcriteria.SortDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OrderServiceImplPositiveTest {

    @Mock
    private OrderRepository repository;
    @Mock
    private ExistChecker checker;
    @Mock
    private MongoTemplate mongoTemplate;
    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder_Success() {
        Long userId = 1L;
        OrderRequest request = new OrderRequest();
        OrderEntity savedEntity = new OrderEntity();
        savedEntity.setId("123");
        savedEntity.setOwner(userId);

        when(repository.save(any(OrderEntity.class))).thenReturn(savedEntity);

        OrderResponse response = orderService.createOrder(userId, request);

        verify(checker).isUserExist(userId);
        verify(repository).save(any(OrderEntity.class));
    }

    @Test
    void testUpdateOrder_Success() {
        long userId = 1L;
        String orderId = "123";
        OrderRequestUpdate updateRequest = new OrderRequestUpdate();
        OrderEntity existingEntity = new OrderEntity();

        when(repository.findById(orderId)).thenReturn(Optional.of(existingEntity));
        when(repository.save(existingEntity)).thenReturn(existingEntity);

        OrderResponse response = orderService.updateOrder(userId, orderId, updateRequest);

        verify(checker).isUserExist(userId);
        verify(checker).isOrderExist(orderId);
        verify(checker).isUserOwnerOfOrder(userId, orderId);
        verify(repository).save(existingEntity);
        assertNotNull(response);
    }

    @Test
    void testGetOrders_Success() {
        long userId = 1L;
        PageRequest pageRequest = PageRequest.of(
                0, 10, Sort.by("id"));
        when(repository.findByOwner(
                eq(userId), any(PageRequest.class))).thenReturn(List.of(new OrderEntity()));

        List<OrderResponse> responses = orderService.getOrders(
                userId, 0, 10, SortByField.CREATION_TIME, SortDirection.ASC);

        assertFalse(responses.isEmpty());
        verify(repository).findByOwner(eq(userId), any(PageRequest.class));
    }

    @Test
    void testSearchOrders_Success() {
        long userId = 1L;
        Query query = new Query();
        when(mongoTemplate.find(
                any(Query.class), eq(OrderEntity.class))).thenReturn(List.of(new OrderEntity()));

        List<OrderResponse> responses = orderService.searchOrders(
                userId, new OrderSearchCriteria(), SortByField.CREATION_TIME, SortDirection.ASC, 0, 10);

        assertFalse(responses.isEmpty());
        verify(mongoTemplate).find(any(Query.class), eq(OrderEntity.class));
    }

    @Test
    void testCloseOrder_Success() {
        long userId = 1L;
        String orderId = "123";
        OrderRequestUpdate updateRequest = new OrderRequestUpdate();
        OrderEntity entity = new OrderEntity();
        entity.setClosedTime(LocalDateTime.now());
        entity.setResult(100);
        entity.setType(LongShort.LONG);
        entity.setProgramCreationTime(LocalDateTime.now());
        entity.setOwner(userId);
        entity.setTicker("ticker");

        when(repository.findById(orderId)).thenReturn(Optional.of(entity));

        orderService.closeOrder(userId, orderId, updateRequest);

        verify(checker).isUserExist(userId);
        verify(checker).isOrderExist(orderId);
        verify(checker).isUserOwnerOfOrder(userId, orderId);
        verify(repository).save(entity);
        verify(rabbitTemplate).convertAndSend(
                anyString(), anyString(), any(StatisticRequest.class));
    }
}
