package oleg.savin.investment.order;

import lombok.extern.slf4j.Slf4j;
import oleg.savin.config.rabbit.RabbitMQConfig;
import oleg.savin.statistic_dto.StatisticRequest;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StatisticSenderService {
    private final RabbitTemplate rabbitTemplate;

    public StatisticSenderService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Async
    public void sendStatistic(StatisticRequest statisticRequest) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.STATISTIC_EXCHANGE,
                    RabbitMQConfig.STATISTIC_ROUTING_KEY,
                    statisticRequest);
        } catch (AmqpException ex) {
            log.error("Failed to send message to RabbitMQ", ex);
        }
    }
}
