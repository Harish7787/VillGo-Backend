package com.VillGo.Controller;

import com.VillGo.DTO.*;
import com.VillGo.DTO.Responce.LoginResponce;
import com.VillGo.DTO.Responce.MessageResponse;
import com.VillGo.Entity.User;
import com.VillGo.Service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody User request) {
        return  authService.register(request);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponce> login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
    @PostMapping("/emailverify")
    public MessageResponse generateOtp(@Valid @RequestBody EmailRequestDTO request) {
        return authService.generateOtp(request);
    }

    @PostMapping("/otp/verify")
    public MessageResponse verifyOtp(@Valid @RequestBody EmailRequestDTO request) {
        return authService.verifyOtp(request);
    }


}
