package com.VillGo.Service.Impl;

import com.VillGo.Config.JWT.JwtService;
import com.VillGo.DTO.*;
import com.VillGo.DTO.Responce.LoginResponce;
import com.VillGo.DTO.Responce.MessageResponse;
import com.VillGo.Entity.User;
import com.VillGo.Repository.OrdersRepository;
import com.VillGo.Repository.UserRepository;
import com.VillGo.Service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JavaMailSender mailSender;
    private final OrdersRepository ordersRepository;

    private final ConcurrentHashMap<String, String> otpStore = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalDateTime> otpExpiryStore = new ConcurrentHashMap<>();

    @Override
    public ResponseEntity<MessageResponse> register(User request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse<>(false, HttpStatus.BAD_REQUEST, "Email already exists", null));
        }

        // Encrypt password
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setFullName(request.getFullName());
        user.setMobile(request.getMobile());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setDeleted(false);
        user.setRole(request.getRole());
        userRepository.save(user);
        return ResponseEntity.ok(
                new MessageResponse<>(true, HttpStatus.OK, "registered successfully", user));
    }

    @Override
    public ResponseEntity<LoginResponce> login(LoginRequest request) {

//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() ->
//                        new RuntimeException("User not found")
//                );
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new LoginResponce<>(false, HttpStatus.NOT_FOUND, "User not found",request.getEmail(),null));
        }

        User user = optionalUser.get();
        //  BLOCK LOGIN IF DELETED
        if (user.isDeleted()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new LoginResponce<>(
                            false,
                            HttpStatus.FORBIDDEN,
                            "Your account has been deactivated..",
                            request.getEmail(),
                            null
                    ));
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponce(
                            false,
                            HttpStatus.BAD_REQUEST,
                            "Invalid username or password",
                            request.getEmail(),null
                    ));
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name(),user.getId());

        return ResponseEntity.ok(
                new LoginResponce<>(
                        true,
                        HttpStatus.OK,
                        "Login successful ",
                        token,
                        user.getRole().name()
                )
        );
    }

    // ================= OTP GENERATE =================
    @Override
    public MessageResponse generateOtp(EmailRequestDTO request) {

//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = generateOtp();

        otpStore.put(request.getEmail(), otp);
        otpExpiryStore.put(request.getEmail(), LocalDateTime.now().plusMinutes(5));

        sendOtpEmail(request.getEmail(), otp);

        return new MessageResponse(true, HttpStatus.OK,
                "OTP sent to email successfully", request.getEmail());
    }

    // ================= OTP VERIFY =================
    @Override
    public MessageResponse verifyOtp(EmailRequestDTO request) {

        String email = request.getEmail();

        if (!otpStore.containsKey(email)) {
            return new MessageResponse(false, HttpStatus.BAD_REQUEST,
                    "OTP not found or expired", null);
        }

        if (otpExpiryStore.get(email).isBefore(LocalDateTime.now())) {
            otpStore.remove(email);
            otpExpiryStore.remove(email);
            return new MessageResponse(false, HttpStatus.BAD_REQUEST,
                    "OTP expired", null);
        }

        if (!otpStore.get(email).equals(request.getOtp())) {
            return new MessageResponse(false, HttpStatus.BAD_REQUEST,
                    "Invalid OTP", null);
        }

        otpStore.remove(email);
        otpExpiryStore.remove(email);

        return new MessageResponse(true, HttpStatus.OK,
                "OTP verified successfully", email);
    }


    // ================= EMAIL SENDER  =================
    private void sendOtpEmail(String toEmail, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Event-Participant | OTP Verification");
        message.setText( "Hello,\n\n" +
                "Your OTP is: " + otp + "\n\n" +
                "This OTP is valid for 5 minutes.\n" +
                "Do not share it with anyone.\n\n" +
                "Regards,\n" +
                "Event-Participant Team".formatted(otp));

        mailSender.send(message);
    }

    // ================= OTP GENERATOR =================
    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

}



