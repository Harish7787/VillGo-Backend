package com.VillGo.Controller;

import com.VillGo.DTO.Responce.MessageResponse;
import com.VillGo.DTO.WishlistRequestDTO;
import com.VillGo.Service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN','USER','SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> create(@RequestBody WishlistRequestDTO dto) {
        return wishlistService.create(dto);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN','USER','SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> list() {
        return wishlistService.list();
    }
    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> remove(@PathVariable Integer id) {
        return wishlistService.remove(id);
    }
    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ADMIN','USER','SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> getWishlistCount() {
        return wishlistService.getWishlistCount();
    }
}
