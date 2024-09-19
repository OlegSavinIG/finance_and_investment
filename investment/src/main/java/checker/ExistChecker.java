package checker;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import oleg.savin.finance.user.UserRepository;
import order.OrderRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistChecker {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public void isUserExist(long userId) {
        boolean existsById = userRepository.existsById(userId);
        if (!existsById) {
            throw new EntityNotFoundException(
                    String.format("User with id:%d not exist", userId));
        }
    }
    public void isOrderExist(long orderId) {
        boolean existsById = orderRepository.existsById(orderId);
        if (!existsById) {
            throw new EntityNotFoundException(
                    String.format("Order with id:%d not exist", orderId));
        }
    }
    public void isUserOwnerOfOrder(long userId, long orderId){
        boolean exists = orderRepository.existsByIdAndOwnerId(orderId, userId);
        if (!exists) {
            throw new EntityNotFoundException(
                    String.format("User with id:%d is not the owner", userId));
        }
    }
}
