package com.VillGo.Controller;

import com.VillGo.DTO.CartRequestDTO;
import com.VillGo.DTO.Responce.MessageResponse;
import com.VillGo.Service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('USER','SUPER_ADMIN','ADMIN')")
    public ResponseEntity<MessageResponse> create(@Valid @RequestBody CartRequestDTO dto) {
        return cartService.create(dto);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('USER','SUPER_ADMIN','ADMIN')")
    public ResponseEntity<MessageResponse> list() {
        return cartService.list();
    }
    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> deleteCartItem(@PathVariable Integer id) {

        return cartService.deleteCartItem(id);
    }
    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ADMIN','USER','SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> getCartCount() {
        return cartService.getCartCount();
    }
}
