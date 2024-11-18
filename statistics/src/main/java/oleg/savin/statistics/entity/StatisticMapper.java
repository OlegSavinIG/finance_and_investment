package oleg.savin.statistics.entity;

import oleg.savin.statistic_dto.StatisticRequest;
import oleg.savin.statistic_dto.StatisticResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.function.Consumer;

@Mapper
public interface StatisticMapper {
    StatisticMapper INSTANCE = Mappers.getMapper(StatisticMapper.class);
    StatisticEntity toEntity(StatisticRequest request);

    StatisticResponse toResponse(StatisticEntity entity);
}
