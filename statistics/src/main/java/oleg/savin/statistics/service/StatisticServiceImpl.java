package oleg.savin.statistics.service;

import lombok.RequiredArgsConstructor;
import oleg.savin.statistic_dto.StatisticRequest;
import oleg.savin.statistic_dto.StatisticResponse;
import oleg.savin.statistics.entity.StatisticEntity;
import oleg.savin.statistics.entity.StatisticMapper;
import oleg.savin.statistics.entity.StatisticSearchCriteria;
import oleg.savin.statistics.repository.StatisticRepository;
import oleg.savin.statistics.service.specification.StatisticSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final StatisticRepository repository;

    @Override
    public void saveStatistic(StatisticRequest request) {
        repository.save(StatisticMapper.INSTANCE.toEntity(request));
    }

    @Override
    public List<StatisticResponse> findByUserId(Long userId) {
        List<StatisticEntity> byUserId = repository.findByUserId(userId);
        return byUserId.stream()
                .map(StatisticMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StatisticResponse> findByCriteria(StatisticSearchCriteria criteria) {
        Specification<StatisticEntity> specification = StatisticSpecification.createSpecification(criteria);
        List<StatisticEntity> statisticEntities = repository.findAll(specification);
        return statisticEntities.stream()
                .map(StatisticMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }
}
