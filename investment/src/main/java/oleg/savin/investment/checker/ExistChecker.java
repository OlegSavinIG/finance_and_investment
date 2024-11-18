package oleg.savin.investment.checker;

import exceptions.OrderNotFoundException;
import exceptions.UnauthorizedOrderAccessException;
import exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import oleg.savin.investment.order.OrderRepository;
import oleg.savin.investment.order.feign.UserClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistChecker {
    private final OrderRepository orderRepository;
    private final UserClient userClient;

    public void isUserExist(long userId) {
        if (!userClient.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

    public void isOrderExist(String orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new OrderNotFoundException(orderId);
        }
    }

    public void isUserOwnerOfOrder(long userId, String orderId) {
        if (!orderRepository.existsByIdAndOwner(orderId, userId)) {
            throw new UnauthorizedOrderAccessException(userId, orderId);
        }
    }
}
