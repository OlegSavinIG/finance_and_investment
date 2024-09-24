package order;

import checker.ExistChecker;
import jakarta.persistence.EntityNotFoundException;
import oleg.savin.finance.user.UserRepository;
import order.searchcriteria.SortByField;
import order.searchcriteria.SortDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import user.UserEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
@ActiveProfiles("test")
//@SpringBootTest
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ExistChecker existChecker;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private UserEntity userEntity;
    private OrderEntity orderEntity;
    private OrderRequest orderRequest;
    private OrderRequestUpdate orderRequestUpdate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userEntity = UserEntity.builder()
                .id(1L)
                .name("Test User")
                .email("testuser@example.com")
                .build();

        orderEntity = OrderEntity.builder()
                .id(1L)
                .ticker("AAPL")
                .type(LongShort.LONG)
                .sum(10000)
                .creationTime(LocalDateTime.now())
                .closedTime(LocalDateTime.now().plusDays(1))
                .result(500)
                .owner(userEntity)
                .build();

        orderRequest = OrderRequest.builder()
                .ticker("AAPL")
                .type(LongShort.LONG)
                .sum(10000)
                .creationTime(LocalDateTime.now())
                .build();

        orderRequestUpdate = OrderRequestUpdate.builder()
                .ticker("GOOGL")
                .sum(15000)
                .closedTime(LocalDateTime.now().plusDays(2))
                .result(700)
                .build();
    }

    @Test
    void createOrder_shouldCreateOrderSuccessfully() {
        when(userRepository.getReferenceById(userEntity.getId())).thenReturn(userEntity);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);

        OrderResponse response = orderService.createOrder(userEntity.getId(), orderRequest);

        verify(existChecker).isUserExist(userEntity.getId());
        verify(orderRepository).save(any(OrderEntity.class));
        assertEquals(orderEntity.getTicker(), response.getTicker());
    }
    @Test
    void createOrder_shouldThrowExceptionWhenUserDoesNotExist() {
        doThrow(new EntityNotFoundException("User not found")).when(existChecker).isUserExist(anyLong());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            orderService.createOrder(1L, orderRequest);
        });

        assertEquals("User not found", exception.getMessage());
    }
    @Test
    void updateOrder_shouldUpdateOrderSuccessfully() {
        when(orderRepository.findById(orderEntity.getId())).thenReturn(Optional.of(orderEntity));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);

        OrderResponse response = orderService.updateOrder(userEntity.getId(), orderEntity.getId(), orderRequestUpdate);

        verify(existChecker).isUserExist(userEntity.getId());
        verify(existChecker).isOrderExist(orderEntity.getId());
        verify(existChecker).isUserOwnerOfOrder(userEntity.getId(), orderEntity.getId());
        verify(orderRepository).save(any(OrderEntity.class));
        assertEquals(orderRequestUpdate.getTicker(), response.getTicker());
    }
    @Test
    void updateOrder_shouldThrowExceptionWhenOrderDoesNotExist() {
        doThrow(new EntityNotFoundException("Order not found")).when(existChecker).isOrderExist(anyLong());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            orderService.updateOrder(userEntity.getId(), orderEntity.getId(), orderRequestUpdate);
        });

        assertEquals("Order not found", exception.getMessage());
    }
    @Test
    void updateOrder_shouldThrowExceptionWhenUserIsNotOwner() {
        doThrow(new EntityNotFoundException("User is not the owner")).when(existChecker).isUserOwnerOfOrder(anyLong(), anyLong());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            orderService.updateOrder(userEntity.getId(), orderEntity.getId(), orderRequestUpdate);
        });

        assertEquals("User is not the owner", exception.getMessage());
    }


    @Test
    void getOrders_shouldReturnPaginatedOrders() {
        Integer from = 0;
        Integer size = 10;
        SortByField sortByField = SortByField.TICKER;
        SortDirection sortDirection = SortDirection.ASC;

        Page<OrderEntity> page = new PageImpl<>(Arrays.asList(orderEntity));

        when(orderRepository.findByOwnerId(eq(userEntity.getId()), any(PageRequest.class))).thenReturn(page);

        List<OrderResponse> responses = orderService.getOrders(userEntity.getId(), from, size, sortByField, sortDirection);

        verify(existChecker).isUserExist(userEntity.getId());
        verify(orderRepository).findByOwnerId(eq(userEntity.getId()), any(PageRequest.class));
        assertEquals(1, responses.size());
        assertEquals(orderEntity.getTicker(), responses.get(0).getTicker());
    }
    @Test
    void getOrders_shouldReturnEmptyListWhenNoOrders() {
        Integer from = 0;
        Integer size = 10;
        SortByField sortByField = SortByField.TICKER;
        SortDirection sortDirection = SortDirection.ASC;

        Page<OrderEntity> page = new PageImpl<>(Collections.emptyList());

        when(orderRepository.findByOwnerId(eq(userEntity.getId()), any(PageRequest.class))).thenReturn(page);

        List<OrderResponse> responses = orderService.getOrders(userEntity.getId(), from, size, sortByField, sortDirection);

        verify(existChecker).isUserExist(userEntity.getId());
        verify(orderRepository).findByOwnerId(eq(userEntity.getId()), any(PageRequest.class));
        assertTrue(responses.isEmpty());
    }
    @Test
    void getOrders_shouldReturnSortedOrders() {
        Integer from = 0;
        Integer size = 10;
        SortByField sortByField = SortByField.SUM;
        SortDirection sortDirection = SortDirection.DESC;

        OrderEntity secondOrder = OrderEntity.builder()
                .id(2L)
                .ticker("GOOGL")
                .type(LongShort.SHORT)
                .sum(20000)
                .creationTime(LocalDateTime.now().minusDays(1))
                .closedTime(LocalDateTime.now().plusDays(2))
                .result(1000)
                .owner(userEntity)
                .build();

        Page<OrderEntity> page = new PageImpl<>(Arrays.asList(orderEntity, secondOrder));

        when(orderRepository.findByOwnerId(eq(userEntity.getId()), any(PageRequest.class))).thenReturn(page);

        List<OrderResponse> responses = orderService.getOrders(userEntity.getId(), from, size, sortByField, sortDirection);

        verify(existChecker).isUserExist(userEntity.getId());
        verify(orderRepository).findByOwnerId(eq(userEntity.getId()), any(PageRequest.class));
        assertEquals(2, responses.size());
        assertEquals("GOOGL", responses.get(0).getTicker());
        assertEquals("AAPL", responses.get(1).getTicker());
    }
}
