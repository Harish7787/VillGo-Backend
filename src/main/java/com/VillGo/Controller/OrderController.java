package com.VillGo.Controller;

import com.VillGo.DTO.BuyRequestDTO;
import com.VillGo.DTO.OrderStatusUpdateDTO;
import com.VillGo.DTO.Responce.MessageResponse;
import com.VillGo.DTO.ReviewRequestDTO;
import com.VillGo.Service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/buy")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','USER')")
    public ResponseEntity<MessageResponse> buy(@Valid @RequestBody BuyRequestDTO dto) {
        return orderService.buy(dto);
    }
    @GetMapping("/customer-reviews")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','USER')")
    public ResponseEntity<MessageResponse> getCustomerReviews() {
        return orderService.getAllReviews();
    }
    // Update order status (admin only)
    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<MessageResponse> updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatusUpdateDTO dto) {
        return orderService.updateOrderStatus(orderId, dto);
    }

    // Add or update review (user only)
    @PutMapping("/{orderId}/review")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponse> addOrUpdateReview(@PathVariable Long orderId, @RequestBody ReviewRequestDTO dto) {
        return orderService.addOrUpdateReview(orderId, dto);
    }
    @GetMapping("/my-orders")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponse> getMyOrders() {
        return orderService.getMyOrders();
    }
    @PutMapping("/cancel/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponse> cancelOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }
    @GetMapping("/getone/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','USER')")
    public ResponseEntity<MessageResponse> getOrderById(@PathVariable("id") Long orderId) {
        return orderService.getOrderById(orderId);
    }
}
