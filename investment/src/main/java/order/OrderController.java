package order;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;
    @PostMapping("/userId")
    public ResponseEntity<OrderResponse> createOrder(
           @NonNull @PathVariable Long userId,
           @NonNull @RequestBody OrderRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createOrder(userId, request));
    }
    @PatchMapping("/userId/orderId")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable Long userId,
            @PathVariable Long orderId,
            @RequestBody OrderRequestUpdate updateRequest
    ) {

    }
}
