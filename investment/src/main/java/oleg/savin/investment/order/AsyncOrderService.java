package oleg.savin.investment.order;

import oleg.savin.investment.entity.OrderEntity;
import oleg.savin.investment.entity.OrderMapper;
import oleg.savin.order_dto.OrderRequest;
import oleg.savin.order_dto.OrderResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
public class AsyncOrderService {
    private final OrderRepository repository;

    public AsyncOrderService(OrderRepository repository) {
        this.repository = repository;
    }

    @Async
    public CompletableFuture<OrderResponse> createOrderAsync(OrderRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            OrderEntity entity = OrderMapper.INSTANCE.toEntity(request);
            OrderEntity saved = repository.save(entity);
            return OrderMapper.INSTANCE.toResponse(saved);
        }).exceptionally(ex -> {
            throw new CompletionException("Error creating order", ex);
        });

    }
}
