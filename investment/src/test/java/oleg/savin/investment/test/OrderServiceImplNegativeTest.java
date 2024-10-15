//package oleg.savin.investment.test;
//
//import oleg.savin.investment.checker.ExistChecker;
//import oleg.savin.investment.order.OrderRepository;
//import oleg.savin.investment.order.OrderServiceImpl;
//import oleg.savin.investment.order.searchcriteria.SortByField;
//import oleg.savin.investment.order.searchcriteria.SortDirection;
//import oleg.savin.investment.entity.OrderEntity;
//import oleg.savin.order_dto.OrderRequest;
//import oleg.savin.order_dto.OrderRequestUpdate;
//import oleg.savin.order_dto.OrderSearchCriteria;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.mongodb.core.MongoTemplate;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.doThrow;
//
//class OrderServiceImplNegativeTest {
//
//    @Mock
//    private OrderRepository repository;
//    @Mock
//    private ExistChecker checker;
//    @Mock
//    private MongoTemplate mongoTemplate;
//
//    @InjectMocks
//    private OrderServiceImpl orderService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCreateOrder_UserDoesNotExist() {
//        // Setup
//        long userId = 1L;
//        OrderRequest request = new OrderRequest();
//
//        doThrow(new IllegalArgumentException("User does not exist")).when(checker).isUserExist(userId);
//
//        // Act & Assert
//        IllegalArgumentException thrown = assertThrows(
//                IllegalArgumentException.class,
//                () -> orderService.createOrder(userId, request),
//                "Expected createOrder() to throw, but it didn't"
//        );
//        assertEquals("User does not exist", thrown.getMessage());
//    }
//
//    @Test
//    void testUpdateOrder_OrderDoesNotExist() {
//        // Setup
//        long userId = 1L;
//        String orderId = "invalidOrderId";
//        OrderRequestUpdate updateRequest = new OrderRequestUpdate();
//
//        doNothing().when(checker).isUserExist(userId);
//        doThrow(new IllegalArgumentException("Order does not exist")).when(checker).isOrderExist(orderId);
//
//        // Act & Assert
//        IllegalArgumentException thrown = assertThrows(
//                IllegalArgumentException.class,
//                () -> orderService.updateOrder(userId, orderId, updateRequest),
//                "Expected updateOrder() to throw, but it didn't"
//        );
//        assertEquals("Order does not exist", thrown.getMessage());
//    }
//
//    @Test
//    void testUpdateOrder_UserIsNotOwner() {
//        // Setup
//        long userId = 1L;
//        String orderId = "orderId";
//        OrderRequestUpdate updateRequest = new OrderRequestUpdate();
//        OrderEntity orderEntity = new OrderEntity();
//
//        doNothing().when(checker).isUserExist(userId);
//        doNothing().when(checker).isOrderExist(orderId);
//        doThrow(new IllegalArgumentException("User is not the owner of this order"))
//                .when(checker).isUserOwnerOfOrder(userId, orderId);
//
//        // Act & Assert
//        IllegalArgumentException thrown = assertThrows(
//                IllegalArgumentException.class,
//                () -> orderService.updateOrder(userId, orderId, updateRequest),
//                "Expected updateOrder() to throw, but it didn't"
//        );
//        assertEquals("User is not the owner of this order", thrown.getMessage());
//    }
//
//    @Test
//    void testSearchOrders_UserDoesNotExist() {
//        // Setup
//        long userId = 1L;
//        OrderSearchCriteria criteria = new OrderSearchCriteria();
//
//        doThrow(new IllegalArgumentException(
//                "User does not exist")).when(checker).isUserExist(userId);
//
//        // Act & Assert
//        IllegalArgumentException thrown = assertThrows(
//                IllegalArgumentException.class,
//                () -> orderService.searchOrders(
//                        userId, criteria, SortByField.CREATION_TIME, SortDirection.ASC, 0, 10),
//                "Expected searchOrders() to throw, but it didn't"
//        );
//        assertEquals("User does not exist", thrown.getMessage());
//    }
//
//    @Test
//    void testCloseOrder_OrderDoesNotExist() {
//        // Setup
//        long userId = 1L;
//        String orderId = "invalidOrderId";
//        OrderRequestUpdate updateRequest = new OrderRequestUpdate();
//
//        doNothing().when(checker).isUserExist(userId);
//        doThrow(new IllegalArgumentException("Order does not exist")).when(checker).isOrderExist(orderId);
//
//        // Act & Assert
//        IllegalArgumentException thrown = assertThrows(
//                IllegalArgumentException.class,
//                () -> orderService.closeOrder(userId, orderId, updateRequest),
//                "Expected closeOrder() to throw, but it didn't"
//        );
//        assertEquals("Order does not exist", thrown.getMessage());
//    }
//
//    @Test
//    void testCloseOrder_UserIsNotOwner() {
//        // Setup
//        long userId = 1L;
//        String orderId = "orderId";
//        OrderRequestUpdate updateRequest = new OrderRequestUpdate();
//        OrderEntity orderEntity = new OrderEntity();
//
//        doNothing().when(checker).isUserExist(userId);
//        doNothing().when(checker).isOrderExist(orderId);
//        doThrow(new IllegalArgumentException("User is not the owner of this order"))
//                .when(checker).isUserOwnerOfOrder(userId, orderId);
//
//        // Act & Assert
//        IllegalArgumentException thrown = assertThrows(
//                IllegalArgumentException.class,
//                () -> orderService.closeOrder(userId, orderId, updateRequest),
//                "Expected closeOrder() to throw, but it didn't"
//        );
//        assertEquals("User is not the owner of this order", thrown.getMessage());
//    }
//}
