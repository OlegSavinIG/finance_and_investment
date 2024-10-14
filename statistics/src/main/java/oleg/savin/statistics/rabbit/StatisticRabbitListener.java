package oleg.savin.statistics.rabbit;

import lombok.RequiredArgsConstructor;
import oleg.savin.statistic_dto.StatisticRequest;
import oleg.savin.statistics.service.StatisticService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import oleg.savin.config.rabbit.RabbitMQConfig;

@Service
@RequiredArgsConstructor
public class StatisticRabbitListener {
    private final StatisticService service;
    @RabbitListener(queues = RabbitMQConfig.STATISTIC_QUEUE)
    public void receiveStatistic(StatisticRequest request) {
        service.saveStatistic(request);
    }
}
