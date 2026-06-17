package com.VillGo.Service;

import com.VillGo.DTO.BuyRequestDTO;
import com.VillGo.DTO.OrderStatusUpdateDTO;
import com.VillGo.DTO.Responce.MessageResponse;
import com.VillGo.DTO.ReviewRequestDTO;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    ResponseEntity<byte[]> downloadOrdersPdf();
    ResponseEntity<MessageResponse> buy(BuyRequestDTO dto);
    ResponseEntity<MessageResponse> getAllReviews();
    ResponseEntity<MessageResponse> updateOrderStatus(Long orderId, OrderStatusUpdateDTO dto);
    MessageResponse cancelOrder(Long orderId);
    ResponseEntity<MessageResponse> addOrUpdateReview(Long orderId, ReviewRequestDTO dto);
    ResponseEntity<MessageResponse> getAllOrders();
    ResponseEntity<MessageResponse> getMyOrders();
    ResponseEntity<MessageResponse> getOrderById(Long orderId);

}
