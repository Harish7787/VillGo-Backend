package com.VillGo.Service.Impl;
import com.VillGo.DTO.CartRequestDTO;
import com.VillGo.DTO.Responce.MessageResponse;
import com.VillGo.Entity.*;
import com.VillGo.Repository.*;
import com.VillGo.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<MessageResponse> create(CartRequestDTO dto) {

        // 1 Get logged-in user's email
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        //  Find User
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(false, HttpStatus.NOT_FOUND, "User not found", null));
        }
        User user = optionalUser.get();

        //  Find Product
        Optional<Product> optionalProduct = productRepository.findById(dto.getProductId());
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(false, HttpStatus.NOT_FOUND, "Product not found", null));
        }
        Product product = optionalProduct.get();

        //  Prevent duplicate product in cart
        if (cartRepository.findByUserAndProduct(user, product).isPresent()) {
            return ResponseEntity.ok(
                    new MessageResponse(false, HttpStatus.OK, "Product already in cart", null)
            );
        }

        //  Save to Cart
        Cart cart = Cart.builder()
                .user(user)
                .product(product)
                .build();
        cartRepository.save(cart);

        //  Return success response
        return ResponseEntity.ok(
                new MessageResponse(true, HttpStatus.OK, "Product added to cart", null)
        );
    }
    @Override
    public ResponseEntity<MessageResponse> getCartCount() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(false, HttpStatus.NOT_FOUND,
                            "User not found", null));
        }

        User user = optionalUser.get();

        long count = cartRepository.countByUser_Id(user.getId());

        return ResponseEntity.ok(
                new MessageResponse(true, HttpStatus.OK,
                        "Cart count fetched successfully", count)
        );
    }
    @Override
    public ResponseEntity<MessageResponse> list() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(false, HttpStatus.NOT_FOUND, "User not found", null));
        }
        User user = optionalUser.get();
        List<Cart> cartList = cartRepository.findByUser(user);

        return ResponseEntity.ok(
                new MessageResponse(true,HttpStatus.OK,"Cart List", cartList)
        );
    }

    @Override
    public ResponseEntity<MessageResponse> deleteCartItem(Integer id) {


            Optional<Cart> optionalCart = cartRepository.findById(id);

            if (optionalCart.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse(
                                false,
                                HttpStatus.NOT_FOUND,
                                "Cart item not found",
                                null
                        ));
            }

            cartRepository.deleteById(id);

            return ResponseEntity.ok(
                    new MessageResponse(
                            true,
                            HttpStatus.OK,
                            "Cart item deleted successfully",
                            null
                    )
            );
        }
    }

