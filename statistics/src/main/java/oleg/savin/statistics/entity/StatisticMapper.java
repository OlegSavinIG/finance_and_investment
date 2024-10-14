package oleg.savin.statistics.entity;

import oleg.savin.statistic_dto.StatisticRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface StatisticMapper {
    StatisticMapper INSTANCE = Mappers.getMapper(StatisticMapper.class);
    StatisticEntity toEntity(StatisticRequest request);
}
