package order;

import order.searchcriteria.SortByField;
import order.searchcriteria.SortDirection;
import org.springframework.stereotype.Service;

import java.util.List;


public interface OrderService {
    OrderResponse createOrder(Long userId, OrderRequest request);

    OrderResponse updateOrder(Long userId, Long orderId, OrderRequestUpdate updateRequest);

    List<OrderResponse> getOrders(Long userId, Integer from, Integer size, SortByField sortByField, SortDirection sortDirection);

    List<OrderResponse> searchOrders(Long userId, OrderSearchCriteria searchCriteria);
}
