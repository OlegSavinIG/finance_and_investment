package oleg.savin.investment.test;

import oleg.savin.investment.order.OrderServiceImpl;
import oleg.savin.investment.order.feign.UserClient;
import oleg.savin.investment.order.searchcriteria.SortByField;
import oleg.savin.investment.order.searchcriteria.SortDirection;
import oleg.savin.models_dto.order.LongShort;
import oleg.savin.models_dto.order.OrderEntity;
import oleg.savin.models_dto.order.OrderRequest;
import oleg.savin.models_dto.order.OrderRequestUpdate;
import oleg.savin.models_dto.order.OrderResponse;
import oleg.savin.models_dto.order.OrderSearchCriteria;
import oleg.savin.models_dto.statistic.StatisticRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.ClickHouseContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import rabbit.RabbitMQConfig;

import java.io.IOException;
import java.util.List;

import static com.mongodb.assertions.Assertions.assertFalse;
import static com.mongodb.assertions.Assertions.assertNull;
import static com.mongodb.internal.connection.tlschannel.util.Util.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers
@Import(RabbitMQConfig.class)
public class OrderServiceImplIntegrationTest {
    @MockBean
    private UserClient userClient;
    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

    private static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.9-management");

    private static ClickHouseContainer clickHouseContainer = new ClickHouseContainer("yandex/clickhouse-server:latest");

    @BeforeAll
    static void setup() {
        mongoDBContainer.start();
        rabbitMQContainer.start();
        clickHouseContainer.start();
    }
    @DynamicPropertySource
    static void registerMongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
    }

    @Test
    void testCreateOrder() {
        when(userClient.existsById(1L)).thenReturn(true);
        OrderRequest request = new OrderRequest();
        request.setTicker("AAPL");
        request.setSum(100);
        request.setType(LongShort.LONG);

        OrderResponse response = orderService.createOrder(1L, request);

        assertNotNull(response);
        assertEquals("AAPL", response.getTicker());
        assertEquals(100, response.getSum());

        OrderEntity savedOrder = mongoTemplate.findById(response.getId(), OrderEntity.class);
        assertNotNull(savedOrder);
        assertEquals("AAPL", savedOrder.getTicker());
    }


    @Test
    void testCloseOrder() {
        when(userClient.existsById(1L)).thenReturn(true);
        OrderRequest request = new OrderRequest();
        request.setTicker("TSLA");
        request.setSum(200);
        request.setType(LongShort.SHORT);
        OrderResponse createdOrder = orderService.createOrder(1L, request);

        OrderRequestUpdate updateRequest = new OrderRequestUpdate();
        updateRequest.setResult(50);

        orderService.closeOrder(1L, createdOrder.getId(), updateRequest);

        OrderEntity closedOrder = mongoTemplate.findById(createdOrder.getId(), OrderEntity.class);
        assertNotNull(closedOrder);
        assertEquals(50, closedOrder.getResult());
        assertEquals("CLOSED", closedOrder.getOrderStatus().toString());
    }
    @Test
    void testUpdateOrder() {
        when(userClient.existsById(1L)).thenReturn(true);

        OrderRequest request = new OrderRequest();
        request.setTicker("AAPL");
        request.setSum(100);
        request.setType(LongShort.LONG);
        OrderResponse createdOrder = orderService.createOrder(1L, request);

        OrderRequestUpdate updateRequest = new OrderRequestUpdate();
        updateRequest.setResult(150);
        updateRequest.setSum(200);

        OrderResponse updatedOrder = orderService.updateOrder(1L, createdOrder.getId(), updateRequest);

        assertNotNull(updatedOrder);
        assertEquals(150, updatedOrder.getResult());
        assertEquals(200, updatedOrder.getSum());
    }
    @Test
    void testSearchOrders() throws InterruptedException {
        when(userClient.existsById(1L)).thenReturn(true);

        createOrderForUser("MSFT", 100, LongShort.LONG, 1L);
        createOrderForUser("AMZN", 200, LongShort.SHORT, 1L);
        Thread.sleep(500);

        OrderSearchCriteria searchCriteria = new OrderSearchCriteria();
        searchCriteria.setTicker("MSFT");
        List<OrderResponse> searchResults = orderService.searchOrders(
                1L, searchCriteria, SortByField.TICKER, SortDirection.ASC, 0, 10);

        assertEquals(1, searchResults.size());
        assertEquals("MSFT", searchResults.get(0).getTicker());
    }

    private void createOrderForUser(String ticker, int sum, LongShort type, Long userId) {
        OrderRequest request = new OrderRequest();
        request.setTicker(ticker);
        request.setSum(sum);
        request.setType(type);
        OrderResponse response = orderService.createOrder(userId, request);
        OrderEntity savedOrder = mongoTemplate.findById(response.getId(), OrderEntity.class);
        System.out.println("Созданный заказ: " + savedOrder);
    }
    @Test
    void testDeleteOrder() {
        when(userClient.existsById(1L)).thenReturn(true);

        OrderRequest request = new OrderRequest();
        request.setTicker("AAPL");
        request.setSum(100);
        request.setType(LongShort.LONG);
        OrderResponse createdOrder = orderService.createOrder(1L, request);

        orderService.deleteOrder(createdOrder.getId());

        OrderEntity deletedOrder = mongoTemplate.findById(createdOrder.getId(), OrderEntity.class);
        assertNull(deletedOrder);
    }
    @Test
    void testStatisticSendingOnCloseOrder() throws IOException, InterruptedException {
        when(userClient.existsById(1L)).thenReturn(true);

        OrderRequest request = new OrderRequest();
        request.setTicker("TSLA");
        request.setSum(200);
        request.setType(LongShort.SHORT);
        OrderResponse createdOrder = orderService.createOrder(1L, request);

        OrderRequestUpdate updateRequest = new OrderRequestUpdate();
        updateRequest.setResult(50);
        orderService.closeOrder(1L, createdOrder.getId(), updateRequest);

        Thread.sleep(500);
        Object statisticMessage = rabbitTemplate.receiveAndConvert("statisticQueue");
        assertNotNull(statisticMessage);
        assertTrue(statisticMessage instanceof StatisticRequest);

        StatisticRequest statistic = (StatisticRequest) statisticMessage;
        assertEquals(1L, statistic.getUserId());
        assertEquals("TSLA", statistic.getTicker());
        assertEquals(50, statistic.getResult());
    }
}
