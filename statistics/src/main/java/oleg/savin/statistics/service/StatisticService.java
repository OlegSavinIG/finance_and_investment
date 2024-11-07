package oleg.savin.statistics.service;


import oleg.savin.statistic_dto.StatisticRequest;
import oleg.savin.statistic_dto.StatisticResponse;
import oleg.savin.statistics.entity.StatisticEntity;
import oleg.savin.statistics.entity.StatisticSearchCriteria;

import java.util.List;

public interface StatisticService {
    void saveStatistic(StatisticRequest request);

    List<StatisticResponse> findByUserId(Long userId);

    List<StatisticResponse> findByCriteria(StatisticSearchCriteria criteria);
}
