package rabbit;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Очередь для статистики
    public static final String STATISTIC_QUEUE = "statisticQueue";
    public static final String STATISTIC_EXCHANGE = "statisticExchange";
    public static final String STATISTIC_ROUTING_KEY = "statistic.routingKey";

    // Очередь для пользователей
    public static final String USER_QUEUE = "userQueue";
    public static final String USER_EXCHANGE = "userExchange";
    public static final String USER_ROUTING_KEY = "user.routingKey";

    // Конфигурация для статистики
    @Bean
    public Queue statisticQueue() {
        return new Queue(STATISTIC_QUEUE, true);
    }

    @Bean
    public TopicExchange statisticExchange() {
        return new TopicExchange(STATISTIC_EXCHANGE);
    }

    @Bean
    public Binding statisticBinding(Queue statisticQueue, TopicExchange statisticExchange) {
        return BindingBuilder.bind(statisticQueue).to(statisticExchange).with(STATISTIC_ROUTING_KEY);
    }

    // Конфигурация для пользователей
    @Bean
    public Queue userQueue() {
        return new Queue(USER_QUEUE, true);
    }

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE);
    }

    @Bean
    public Binding userBinding(Queue userQueue, TopicExchange userExchange) {
        return BindingBuilder.bind(userQueue).to(userExchange).with(USER_ROUTING_KEY);
    }
}

