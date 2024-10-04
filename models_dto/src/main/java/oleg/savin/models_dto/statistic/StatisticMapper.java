package oleg.savin.models_dto.statistic;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface StatisticMapper {
    StatisticMapper INSTANCE = Mappers.getMapper(StatisticMapper.class);
    StatisticEntity toEntity(StatisticRequest request);
}
