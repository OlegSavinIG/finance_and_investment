package oleg.savin.investment.test;

import oleg.savin.investment.order.OrderServiceImpl;
import oleg.savin.investment.order.feign.UserClient;
import oleg.savin.models_dto.order.LongShort;
import oleg.savin.models_dto.order.OrderEntity;
import oleg.savin.models_dto.order.OrderRequest;
import oleg.savin.models_dto.order.OrderRequestUpdate;
import oleg.savin.models_dto.order.OrderResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.ClickHouseContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers
public class OrderServiceImplIntegrationTest {
    @MockBean
    private UserClient userClient;
    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Поднимаем MongoDB контейнер
    private static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

    // Поднимаем RabbitMQ контейнер
    private static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.9-management");

    // Поднимаем ClickHouse контейнер
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
        // Arrange
        OrderRequest request = new OrderRequest();
        request.setTicker("AAPL");
        request.setSum(100);
        request.setType(LongShort.LONG);

        // Act
        OrderResponse response = orderService.createOrder(1L, request);

        // Assert
        assertNotNull(response);
        assertEquals("AAPL", response.getTicker());
        assertEquals(100, response.getSum());

        // Ensure order is saved in MongoDB
        OrderEntity savedOrder = mongoTemplate.findById(response.getId(), OrderEntity.class);
        assertNotNull(savedOrder);
        assertEquals("AAPL", savedOrder.getTicker());
    }

    @Test
    void testCloseOrder() {
        when(userClient.existsById(1L)).thenReturn(true);
        // Arrange
        OrderRequest request = new OrderRequest();
        request.setTicker("TSLA");
        request.setSum(200);
        request.setType(LongShort.SHORT);
        OrderResponse createdOrder = orderService.createOrder(1L, request);

        OrderRequestUpdate updateRequest = new OrderRequestUpdate();
        updateRequest.setResult(50);

        // Act
        orderService.closeOrder(1L, createdOrder.getId(), updateRequest);

        // Assert
        OrderEntity closedOrder = mongoTemplate.findById(createdOrder.getId(), OrderEntity.class);
        assertNotNull(closedOrder);
        assertEquals(50, closedOrder.getResult());
        assertEquals("CLOSED", closedOrder.getOrderStatus().toString());

        // Verify message is sent to RabbitMQ
        // Проверка, что сообщение отправлено в RabbitMQ
        // Здесь можно использовать Mockito для валидации отправки сообщений
    }
}
