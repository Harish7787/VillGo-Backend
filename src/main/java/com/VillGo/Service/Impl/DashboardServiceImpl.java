package com.VillGo.Service.Impl;


import com.VillGo.DTO.Responce.MessageResponse;
import com.VillGo.Entity.Orders;
import com.VillGo.Repository.OrdersRepository;
import com.VillGo.Repository.ProductRepository;
import com.VillGo.Repository.UserRepository;
import com.VillGo.Service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ProductRepository productRepository;
    private final OrdersRepository ordersRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<MessageResponse> getTotalProducts() {

        long total = productRepository.count();

        return ResponseEntity.ok(
                new MessageResponse(true, HttpStatus.OK, "Total products", total)
        );
    }

    @Override
    public ResponseEntity<MessageResponse> getTotalOrders() {

        long total = ordersRepository.count();

        return ResponseEntity.ok(
                new MessageResponse(true, HttpStatus.OK, "Total orders", total)
        );
    }

    @Override
    public ResponseEntity<MessageResponse> getTotalCustomers() {

        long total = userRepository.countByRole("USER");

        return ResponseEntity.ok(
                new MessageResponse(true, HttpStatus.OK, "Total customers", total)
        );
    }

    @Override
    public ResponseEntity<MessageResponse> getLatestOrders() {

        List<Orders> latestOrders = ordersRepository.findTop5ByOrderByIdDesc();

        return ResponseEntity.ok(
                new MessageResponse(true, HttpStatus.OK, "Latest orders", latestOrders)
        );
    }
}