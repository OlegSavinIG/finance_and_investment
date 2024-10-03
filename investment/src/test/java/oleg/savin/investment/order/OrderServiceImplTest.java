package oleg.savin.investment.order;

import oleg.savin.models_dto.order.LongShort;
import oleg.savin.models_dto.order.OrderEntity;
import oleg.savin.models_dto.order.OrderRequest;
import oleg.savin.models_dto.order.OrderResponse;
import oleg.savin.models_dto.order.OrderSearchCriteria;
import oleg.savin.models_dto.order.OrderStatus;
import oleg.savin.models_dto.statistic.StatisticEntity;
import oleg.savin.investment.order.searchcriteria.SortByField;
import oleg.savin.investment.order.searchcriteria.SortDirection;
import oleg.savin.models_dto.order.OrderRequestUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import oleg.savin.statistics.service.StatisticRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;


    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private StatisticRepository statisticRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderEntity orderEntity;
    private OrderRequest orderRequest;
    private OrderRequestUpdate orderRequestUpdate;

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
                .programCreationTime(LocalDateTime.now())
                .build();

        orderRequest = OrderRequest.builder()
                .ticker("AAPL")
                .type(LongShort.LONG)
                .sum(1000)
                .creationTime(LocalDateTime.now())
                .build();

        orderRequestUpdate = OrderRequestUpdate.builder()
                .ticker("TSLA")
                .sum(2000)
                .build();
    }

    @Test
    void testCreateOrder() {
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);

        OrderResponse response = orderService.createOrder(1L, orderRequest);

        assertNotNull(response);
        assertEquals("AAPL", response.getTicker());
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void testUpdateOrder() {
        when(orderRepository.findById("1")).thenReturn(Optional.of(orderEntity));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);

        OrderResponse response = orderService.updateOrder(1L, "1", orderRequestUpdate);

        assertNotNull(response);
        assertEquals("TSLA", response.getTicker());
        verify(orderRepository, times(1)).findById("1");
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void testGetOrders() {
        when(orderRepository.findByOwnerId(anyLong(), any(PageRequest.class)))
                .thenReturn(Stream.of(orderEntity).collect(Collectors.toList()));

        List<OrderResponse> orders = orderService.getOrders(1L, 0, 10, SortByField.TICKER, SortDirection.ASC);

        assertNotNull(orders);
        assertEquals(1, orders.size());
        verify(orderRepository, times(1)).findByOwnerId(anyLong(), any(PageRequest.class));
    }

    @Test
    void testSearchOrders() {
        when(mongoTemplate.find(any(Query.class), eq(OrderEntity.class)))
                .thenReturn(List.of(orderEntity));

        List<OrderResponse> orders = orderService.searchOrders(1L, new OrderSearchCriteria(),
                SortByField.TICKER, SortDirection.ASC, 0, 10);

        assertNotNull(orders);
        assertEquals(1, orders.size());
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(OrderEntity.class));
    }

    @Test
    void testCloseOrder() {
        when(orderRepository.findById("1")).thenReturn(Optional.of(orderEntity));

        orderService.closeOrder(1L, "1", orderRequestUpdate);

        assertEquals(OrderStatus.CLOSED, orderEntity.getOrderStatus());
        verify(orderRepository, times(1)).findById("1");
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
        verify(statisticRepository, times(1)).saveStatistic(any(StatisticEntity.class));
    }

    @Test
    void testDeleteOrder() {
        orderService.deleteOrder("1");

        verify(mongoTemplate, times(1)).remove("1");
    }
}
