package oleg.savin.investment.checker;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import oleg.savin.investment.order.OrderRepository;
import oleg.savin.investment.order.feign.UserClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class ExistChecker {
    private final OrderRepository orderRepository;
    private final UserClient userClient;

    public void isUserExist(long userId) {
        boolean existsById = userClient.existsById(userId);
        if (!existsById) {
            throw new EntityNotFoundException(
                    String.format("User with id:%d not exist", userId));
        }
    }
    public void isOrderExist(String orderId) {
        boolean existsById = orderRepository.existsById(orderId);
        if (!existsById) {
            throw new EntityNotFoundException(
                    String.format("Order with id:%d not exist", orderId));
        }
    }
    public void isUserOwnerOfOrder(long userId, String orderId){
        boolean exists = orderRepository.existsByIdAndOwner(orderId, userId);
        if (!exists) {
            throw new EntityNotFoundException(
                    String.format("User with id:%d is not the owner", userId));
        }
    }
}
