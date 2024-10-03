package oleg.savin.statistics.service;

import oleg.savin.models_dto.statistic.StatisticRequest;

public interface StatisticService {
    void saveStatistic(StatisticRequest request);
}
