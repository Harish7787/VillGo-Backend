package com.VillGo.Controller;

import com.VillGo.DTO.Responce.MessageResponse;
import com.VillGo.Service.DashboardService;
import com.VillGo.Service.OrderService;
import com.VillGo.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final OrderService orderService;
    private final ProductService productService;

    @GetMapping("/total-products")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> totalProducts() {
        return dashboardService.getTotalProducts();
    }

    @GetMapping("/total-orders")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> totalOrders() {
        return dashboardService.getTotalOrders();
    }

    @GetMapping("/total-customers")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> totalCustomers() {
        return dashboardService.getTotalCustomers();
    }

    @GetMapping("/latest-orders")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> latestOrders() {
        return dashboardService.getLatestOrders();
    }

    @GetMapping("/download-pdf")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<byte[]> downloadOrdersPdf() {
        return orderService.downloadOrdersPdf();
    }
    @GetMapping("/download-prduct-pdf")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<byte[]> downloadProductsPdf() {
        return productService.downloadProductsPdf();
    }
}