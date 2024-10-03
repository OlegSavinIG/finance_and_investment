//package oleg.savin.order;
//
//import oleg.savin.checker.ExistChecker;
//import oleg.savin.models_dto.order.*;
//import oleg.savin.models_dto.statistic.StatisticEntity;
//import oleg.savin.order.searchcriteria.SortDirection;
//import oleg.savin.order.searchcriteria.SortByField;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties;
//import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import service.StatisticRepository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//@SpringBootTest(classes = WavefrontProperties.Application.class)
//@ExtendWith(SpringExtension.class)
//class OrderServiceImplIntegrationTest {
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @MockBean
//    private ExistChecker existChecker;
//
//    @MockBean
//    private StatisticRepository statisticRepository;
//
//    @MockBean
//    private MongoTemplate mongoTemplate;
//
//    private OrderServiceImpl orderService;
//
//    @BeforeEach
//    void setUp() {
//        orderService = new OrderServiceImpl(
//                orderRepository, existChecker, mongoTemplate, statisticRepository);
//    }
//
//    @Test
//    void testCreateOrder() {
//        // Mock проверка на существование пользователя
//        Mockito.doNothing().when(existChecker).isUserExist(1L);
//
//        // Пример данных для создания заказа
//        OrderRequest orderRequest = new OrderRequest();
//        orderRequest.setTicker("AAPL");
//        orderRequest.setType(LongShort.LONG);
//        orderRequest.setSum(1000);
//
//        OrderEntity savedEntity = OrderEntity.builder()
//                .id("1")
//                .owner(1L)
//                .orderStatus(OrderStatus.OPEN)
//                .programCreationTime(LocalDateTime.now())
//                .build();
//
//        // Мокирование сохранения заказа в репозитории
//        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedEntity);
//
//        // Вызов сервиса
//        OrderResponse response = orderService.createOrder(1L, orderRequest);
//
//        // Проверка ответа
//        assertThat(response).isNotNull();
////        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.OPEN);
////        assertThat(response.getOwner()).isEqualTo(1L);
//    }
//
//    @Test
//    void testUpdateOrder() {
//        // Mock проверка на существование пользователя и заказа
//        Mockito.doNothing().when(existChecker).isUserExist(1L);
//        Mockito.doNothing().when(existChecker).isOrderExist("1");
//        Mockito.doNothing().when(existChecker).isUserOwnerOfOrder(1L, "1");
//
//        OrderEntity existingOrder = OrderEntity.builder()
//                .id("1")
//                .owner(1L)
//                .orderStatus(OrderStatus.OPEN)
//                .build();
//
//        OrderRequestUpdate updateRequest = new OrderRequestUpdate();
//        updateRequest.setTicker("TSLA");
//
//        // Мокирование поиска и сохранения заказа в репозитории
//        when(orderRepository.findById("1")).thenReturn(Optional.of(existingOrder));
//        when(orderRepository.save(any(OrderEntity.class))).thenReturn(existingOrder);
//
//        // Вызов сервиса
//        OrderResponse response = orderService.updateOrder(1L, "1", updateRequest);
//
//        // Проверка обновленного заказа
//        assertThat(response).isNotNull();
//        assertThat(response.getTicker()).isEqualTo("TSLA");
//    }
//
//    @Test
//    void testCloseOrder() {
//        // Mock проверки на существование пользователя и заказа
//        Mockito.doNothing().when(existChecker).isUserExist(1L);
//        Mockito.doNothing().when(existChecker).isOrderExist("1");
//        Mockito.doNothing().when(existChecker).isUserOwnerOfOrder(1L, "1");
//
//        // Пример данных для закрытия заказа
//        OrderEntity order = OrderEntity.builder()
//                .id("1")
//                .owner(1L)
//                .orderStatus(OrderStatus.OPEN)
//                .build();
//
//        when(orderRepository.findById("1")).thenReturn(Optional.of(order));
//        when(orderRepository.save(any(OrderEntity.class))).thenReturn(order);
//
//        // Вызов метода closeOrder
//        orderService.closeOrder(1L, "1", new OrderRequestUpdate());
//
//        // Проверка изменений статуса заказа
//        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CLOSED);
//        Mockito.verify(statisticRepository, Mockito.times(1))
//                .saveStatistic(any(StatisticEntity.class));
//    }
//
//    @Test
//    void testSearchOrders() {
//        // Мокирование поиска заказов по критериям
//        when(mongoTemplate.find(any(Query.class), any()))
//                .thenReturn(List.of(OrderEntity.builder().build()));
//
//        List<OrderResponse> results = orderService.searchOrders(
//                1L, new OrderSearchCriteria(), SortByField.CREATION_TIME, SortDirection.ASC, 0, 10);
//
//        // Проверка результатов
//        assertThat(results).isNotEmpty();
//    }
//}
