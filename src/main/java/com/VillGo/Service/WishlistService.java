package com.VillGo.Service;

import com.VillGo.DTO.Responce.MessageResponse;
import com.VillGo.DTO.WishlistRequestDTO;
import org.springframework.http.ResponseEntity;

public interface WishlistService {

    ResponseEntity<MessageResponse> create(WishlistRequestDTO dto);
    ResponseEntity<MessageResponse> getWishlistCount();
    ResponseEntity<MessageResponse> remove(int id);
    ResponseEntity<MessageResponse> list();
}
