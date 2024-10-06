package rabbit;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String STATISTIC_QUEUE = "statisticQueue";
    public static final String STATISTIC_EXCHANGE = "statisticExchange";
    public static final String STATISTIC_ROUTING_KEY = "statistic.routingKey";

    public static final String USER_QUEUE = "userQueue";
    public static final String USER_EXCHANGE = "userExchange";
    public static final String USER_ROUTING_KEY = "user.routingKey";
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

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

