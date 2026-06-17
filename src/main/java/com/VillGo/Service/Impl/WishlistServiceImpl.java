package com.VillGo.Service.Impl;

import com.VillGo.DTO.Responce.MessageResponse;
import com.VillGo.DTO.Responce.WishlistResponse;
import com.VillGo.DTO.WishlistRequestDTO;
import com.VillGo.Entity.Product;
import com.VillGo.Entity.User;
import com.VillGo.Entity.Wishlist;
import com.VillGo.Repository.ProductRepository;
import com.VillGo.Repository.UserRepository;
import com.VillGo.Repository.WishlistRepository;
import com.VillGo.Service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

//    @Override
//    public ResponseEntity<MessageResponse> create(WishlistRequestDTO dto) {
//
//        Product product = productRepository.findById(dto.getProductId())
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // Prevent duplicate
//        if (wishlistRepository.existsByUserIdAndProductId(user.getId(), product.getId())) {
//            return ResponseEntity.ok(new MessageResponse(false, HttpStatus.BAD_REQUEST,"Product already in wishlist",product));
//        }
//
//        Wishlist wishlist = Wishlist.builder()
//                .user(user)
//                .product(product)
//                .build();
//
//        wishlistRepository.save(wishlist);
//
//        return ResponseEntity.ok(new MessageResponse(true,HttpStatus.OK,"Product added to wishlist",wishlist));
//    }
@Override
public ResponseEntity<MessageResponse> create(WishlistRequestDTO dto) {

    Product product = productRepository.findById(dto.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not found"));

    String email = SecurityContextHolder.getContext().getAuthentication().getName();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (wishlistRepository.existsByUserIdAndProductId(user.getId(), product.getId())) {
        return ResponseEntity.ok(
                new MessageResponse(false, HttpStatus.BAD_REQUEST,
                        "Product already in wishlist", null));
    }

    Wishlist wishlist = Wishlist.builder()
            .user(user)
            .product(product)
            .build();

    wishlistRepository.save(wishlist);

    // Create clean response
    WishlistResponse response = new WishlistResponse();
    response.setWishlistId(wishlist.getId());
    response.setProductId(product.getId());
    response.setProductName(product.getName());
    response.setPrice(product.getPrice());
    response.setImage(product.getImage());

    return ResponseEntity.ok(
            new MessageResponse(true, HttpStatus.OK,
                    "Product added to wishlist", response)
    );
}
    @Override
    public ResponseEntity<MessageResponse> getWishlistCount() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(false, HttpStatus.NOT_FOUND,
                            "User not found", null));
        }

        User user = optionalUser.get();

        long count = wishlistRepository.countByUser_Id(user.getId());

        return ResponseEntity.ok(
                new MessageResponse(true, HttpStatus.OK,
                        "Wishlist count fetched successfully", count)
        );
    }

@Override
public ResponseEntity<MessageResponse> list() {

    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    Optional<User> optionalUser = userRepository.findByEmail(email);

    if (optionalUser.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponse(false, HttpStatus.NOT_FOUND,
                        "User not found", null));
    }

    User user = optionalUser.get();

    List<Wishlist> wishlist = wishlist = wishlistRepository.findByUser(user);

    List<WishlistResponse> response = wishlist.stream().map(w -> {
        WishlistResponse dto = new WishlistResponse();
        dto.setWishlistId(w.getId());
        dto.setProductId(w.getProduct().getId());
        dto.setProductName(w.getProduct().getName());
        dto.setImage(w.getProduct().getImage());
        return dto;
    }).toList();

    return ResponseEntity.ok(
            new MessageResponse(true, HttpStatus.OK,
                    "Wishlist fetched successfully", response)
    );
}
    @Override
    public ResponseEntity<MessageResponse> remove(int id) {
// Get logged-in user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(false, HttpStatus.NOT_FOUND,
                            "User not found", null));
        }

        User user = optionalUser.get();

        // Find wishlist item
        Optional<Wishlist> optionalWishlist =
                wishlistRepository.findByUser_IdAndProduct_Id(user.getId(), id);

        if (optionalWishlist.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(false, HttpStatus.BAD_REQUEST,
                            "Product not found in wishlist", null));
        }

        wishlistRepository.delete(optionalWishlist.get());

        return ResponseEntity.ok(
                new MessageResponse(true, HttpStatus.OK,
                        "Product removed from wishlist successfully", null)
        );
    }
}
