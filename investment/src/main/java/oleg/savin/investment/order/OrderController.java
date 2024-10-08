package oleg.savin.investment.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import oleg.savin.models_dto.order.OrderRequest;
import oleg.savin.models_dto.order.OrderResponse;
import oleg.savin.models_dto.order.OrderSearchCriteria;
import oleg.savin.investment.order.searchcriteria.SortByField;
import oleg.savin.investment.order.searchcriteria.SortDirection;
import oleg.savin.models_dto.order.OrderRequestUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    @PostMapping("/{userId}")
    public ResponseEntity<OrderResponse> createOrder(
           @NonNull @PathVariable Long userId,
           @Valid @RequestBody OrderRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createOrder(userId, request));
    }
    @PatchMapping("/{userId}/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable Long userId,
            @PathVariable String orderId,
            @Valid @RequestBody OrderRequestUpdate updateRequest
    ) {
        return ResponseEntity.ok(
                service.updateOrder(userId, orderId, updateRequest));
    }
    @PatchMapping("/close/{userId}/{orderId}")
    public ResponseEntity<HttpStatus> closeOrder(
            @PathVariable Long userId,
            @PathVariable String orderId,
            @Valid @RequestBody OrderRequestUpdate requestUpdate
    ) {
        service.closeOrder(userId, orderId, requestUpdate);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrders(
            @PathVariable Long userId,
            @PositiveOrZero @RequestParam(defaultValue = "0")
            Integer from,
            @Positive @RequestParam(defaultValue = "10")
            Integer size,
            @RequestParam(defaultValue = "CREATION_TIME") SortByField sortByField,
            @RequestParam(defaultValue = "ASC") SortDirection sortDirection
            ) {
        return ResponseEntity.ok(
                service.getOrders(userId, from, size, sortByField, sortDirection));
    }
    @GetMapping("/{userId}/search")
    public ResponseEntity<List<OrderResponse>> searchOrders(
            @PathVariable Long userId,
            @ModelAttribute OrderSearchCriteria searchCriteria,
            @PositiveOrZero @RequestParam(defaultValue = "0")
            Integer from,
            @Positive @RequestParam(defaultValue = "10")
            Integer size,
            @RequestParam(defaultValue = "CREATION_TIME")SortByField sortByField,
            @RequestParam(defaultValue = "ASC")SortDirection sortDirection
    ) {
        return ResponseEntity.ok(
                service.searchOrders(userId, searchCriteria, sortByField, sortDirection, from, size));
    }
    @DeleteMapping("/{orderId}")
    public ResponseEntity<HttpStatus> deleteOrder(
            @PathVariable String orderId
    ) {
        service.deleteOrder(orderId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
