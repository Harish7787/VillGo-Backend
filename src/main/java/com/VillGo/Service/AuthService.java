package com.VillGo.Service;

import com.VillGo.DTO.*;
import com.VillGo.DTO.Responce.LoginResponce;
import com.VillGo.DTO.Responce.MessageResponse;
import com.VillGo.Entity.User;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<MessageResponse> register(User request);
    ResponseEntity<LoginResponce> login(LoginRequest request);
    MessageResponse generateOtp(EmailRequestDTO request);
    MessageResponse verifyOtp(EmailRequestDTO request);

}
