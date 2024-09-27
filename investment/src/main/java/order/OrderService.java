package order;

import order.searchcriteria.SortByField;
import order.searchcriteria.SortDirection;

import java.util.List;


public interface OrderService {
    OrderResponse createOrder(Long userId, OrderRequest request);

    OrderResponse updateOrder(Long userId, String orderId, OrderRequestUpdate updateRequest);

    List<OrderResponse> getOrders(
            Long userId, Integer from, Integer size,
            SortByField sortByField, SortDirection sortDirection);

    List<OrderResponse> searchOrders(
            Long userId, OrderSearchCriteria searchCriteria, SortByField sortByField,
            SortDirection sortDirection, Integer from, Integer size);

    void deleteOrder(String orderId);

    void closeOrder(Long userId, String orderId, OrderRequestUpdate requestUpdate);
}
