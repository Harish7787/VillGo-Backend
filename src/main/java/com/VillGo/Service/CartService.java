package com.VillGo.Service;

import com.VillGo.DTO.CartRequestDTO;
import com.VillGo.DTO.Responce.MessageResponse;
import org.springframework.http.ResponseEntity;

public interface CartService {

    ResponseEntity<MessageResponse> create(CartRequestDTO dto);
    ResponseEntity<MessageResponse> getCartCount();
    ResponseEntity<MessageResponse> list();
    ResponseEntity<MessageResponse> deleteCartItem(Integer id);
}
