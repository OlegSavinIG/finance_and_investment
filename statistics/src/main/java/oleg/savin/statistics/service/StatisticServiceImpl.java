package oleg.savin.statistics.service;

import lombok.RequiredArgsConstructor;
import oleg.savin.statistic_dto.StatisticRequest;
import oleg.savin.statistics.entity.StatisticMapper;
import oleg.savin.statistics.repository.StatisticRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final StatisticRepository repository;
    @Override
    public void saveStatistic(StatisticRequest request) {
        repository.saveStatistic(StatisticMapper.INSTANCE.toEntity(request));
    }
}
