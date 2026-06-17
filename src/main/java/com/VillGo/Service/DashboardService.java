package com.VillGo.Service;

import com.VillGo.DTO.Responce.MessageResponse;
import org.springframework.http.ResponseEntity;

public interface DashboardService {
    ResponseEntity<MessageResponse> getTotalProducts();

    ResponseEntity<MessageResponse> getTotalOrders();

    ResponseEntity<MessageResponse> getTotalCustomers();

    ResponseEntity<MessageResponse> getLatestOrders();
}
