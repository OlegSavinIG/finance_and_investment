package oleg.savin.investment.order;

import oleg.savin.investment.checker.ExistChecker;
import oleg.savin.models_dto.order.LongShort;
import oleg.savin.models_dto.order.OrderEntity;
import oleg.savin.models_dto.order.OrderRequest;
import oleg.savin.models_dto.order.OrderRequestUpdate;
import oleg.savin.models_dto.order.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import oleg.savin.statistics.service.StatisticRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplNegativeTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ExistChecker existChecker;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private StatisticRepository statisticRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderEntity orderEntity;
    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        orderEntity = OrderEntity.builder()
                .id("1")
                .owner(1L)
                .ticker("AAPL")
                .type(LongShort.LONG)
                .sum(1000)
                .orderStatus(OrderStatus.OPEN)
                .build();

        orderRequest = OrderRequest.builder()
                .ticker("AAPL")
                .type(LongShort.LONG)
                .sum(1000)
                .build();
    }

    @Test
    void testCreateOrderUserDoesNotExist() {
        doThrow(new IllegalArgumentException("User does not exist"))
                .when(existChecker).isUserExist(1L);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(1L, orderRequest);
        });

        assertEquals("User does not exist", exception.getMessage());
        verify(orderRepository, never()).save(any(OrderEntity.class));
    }

    @Test
    void testUpdateOrderOrderDoesNotExist() {
        doThrow(new IllegalArgumentException("Order does not exist"))
                .when(existChecker).isOrderExist("1");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.updateOrder(1L, "1", new OrderRequestUpdate());
        });

        assertEquals("Order does not exist", exception.getMessage());
        verify(orderRepository, never()).save(any(OrderEntity.class));
    }

    @Test
    void testUpdateOrderUserIsNotOwner() {
        when(orderRepository.findById("1")).thenReturn(Optional.of(orderEntity));
        doThrow(new IllegalStateException("User is not owner of the order"))
                .when(existChecker).isUserOwnerOfOrder(2L, "1");

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            orderService.updateOrder(2L, "1", new OrderRequestUpdate());
        });

        assertEquals("User is not owner of the order", exception.getMessage());
        verify(orderRepository, never()).save(any(OrderEntity.class));
    }

    @Test
    void testCloseOrderUserDoesNotExist() {
        doThrow(new IllegalArgumentException("User does not exist"))
                .when(existChecker).isUserExist(1L);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.closeOrder(1L, "1", new OrderRequestUpdate());
        });

        assertEquals("User does not exist", exception.getMessage());
        verify(orderRepository, never()).save(any(OrderEntity.class));
    }

    @Test
    void testCloseOrderOrderDoesNotExist() {
        doThrow(new IllegalArgumentException("Order does not exist"))
                .when(existChecker).isOrderExist("1");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.closeOrder(1L, "1", new OrderRequestUpdate());
        });

        assertEquals("Order does not exist", exception.getMessage());
        verify(orderRepository, never()).save(any(OrderEntity.class));
    }

    @Test
    void testCloseOrderUserIsNotOwner() {
        when(orderRepository.findById("1")).thenReturn(Optional.of(orderEntity));
        doThrow(new IllegalStateException("User is not owner of the order"))
                .when(existChecker).isUserOwnerOfOrder(2L, "1");

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            orderService.closeOrder(2L, "1", new OrderRequestUpdate());
        });

        assertEquals("User is not owner of the order", exception.getMessage());
        verify(orderRepository, never()).save(any(OrderEntity.class));
    }
}
