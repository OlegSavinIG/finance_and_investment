package order;

import org.springframework.stereotype.Service;


public interface OrderService {
    OrderResponse createOrder(Long userId, OrderRequest request);
}
