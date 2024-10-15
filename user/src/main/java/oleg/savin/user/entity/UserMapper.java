package oleg.savin.user.entity;

import oleg.savin.order_dto.OrderRequest;
import oleg.savin.order_dto.OrderResponse;
import oleg.savin.user_dto.UserRequest;
import oleg.savin.user_dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity toEntity(UserRequest request);

    UserRequest toRequest(UserEntity entity);
    UserResponse toResponse(UserEntity entity);

}
