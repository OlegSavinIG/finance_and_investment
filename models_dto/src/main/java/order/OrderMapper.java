package order;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderEntity toEntity(OrderRequest request);

    OrderRequest toRequest(OrderEntity entity);
    OrderResponse toResponse(OrderEntity entity);
}
