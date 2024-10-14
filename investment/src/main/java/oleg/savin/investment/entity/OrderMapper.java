package oleg.savin.investment.entity;

import oleg.savin.order_dto.OrderRequest;
import oleg.savin.order_dto.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderEntity toEntity(OrderRequest request);

    OrderRequest toRequest(OrderEntity entity);
    OrderResponse toResponse(OrderEntity entity);
}
