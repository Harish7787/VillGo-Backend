package com.VillGo.Controller;

import com.VillGo.DTO.Responce.*;
import com.VillGo.Entity.User;
import com.VillGo.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/superauth")
@RequiredArgsConstructor
public class SuperController {
    private final BrandService brandService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping("/products/getall")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<DataResponse<List<ProductResponse>>> getAll() {
        return productService.getAll();
    }
    @GetMapping("/categories/getall")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<ListResponse<CategoryResponse>> getAllCategory() {
        return categoryService.getAll();
    }
    @GetMapping("/brands/getall")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<ListResponse<BrandResponse>> getAllBrands() {
        return brandService.getAll();
    }
    @GetMapping("/orders/getall")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<MessageResponse> getAllOrders() {
        return orderService.getAllOrders();
    }
    @GetMapping("/admin/getall")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<MessageResponse<List<User>>> getAllAdmin() {
        return userService.getAllAdmins();
    }
    @GetMapping("/admin/customers")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<List<User>> getCustomers() {
        return userService.getAllCustomersWithReceivedOrders();
    }

}
