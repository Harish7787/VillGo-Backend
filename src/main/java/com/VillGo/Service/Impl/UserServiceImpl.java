package com.VillGo.Service.Impl;

import com.VillGo.Config.JWT.JwtService;
import com.VillGo.DTO.ForgotPasswordDTO;
import com.VillGo.DTO.ResetPasswordRequestDTO;
import com.VillGo.DTO.Responce.MessageResponse;
import com.VillGo.DTO.UpdateDTO.UpdateUserDTO;
import com.VillGo.Entity.Orders;
import com.VillGo.Entity.User;
import com.VillGo.Exception.DuplicateResourceException;
import com.VillGo.Exception.ResourceNotFoundException;
import com.VillGo.Repository.OrdersRepository;
import com.VillGo.Repository.UserRepository;
import com.VillGo.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JavaMailSender mailSender;
    private final OrdersRepository ordersRepository;
    private final ConcurrentHashMap<String, String> otpStore = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalDateTime> otpExpiryStore = new ConcurrentHashMap<>();
    @Override
    public ResponseEntity<MessageResponse<List<User>>> getAllAdmins() {

        List<User> admins = userRepository.findByRoleAndDeletedFalse("ADMIN");

        if (admins.isEmpty()) {
            return ResponseEntity.ok(
                    new MessageResponse<>(
                            false,
                            HttpStatus.NO_CONTENT,
                            "No admins found",
                            null
                    )
            );
        }

        return ResponseEntity.ok(
                new MessageResponse<>(
                        true,
                        HttpStatus.OK,
                        "Admins fetched successfully",
                        admins
                )
        );
    }

    @Override
    public ResponseEntity<MessageResponse> updateUser(Integer id, UpdateUserDTO dto) {

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(
                            false,
                            HttpStatus.NOT_FOUND,
                            "User not found",
                            null
                    ));
        }

        User user = optionalUser.get();

        if (dto.getFullName() != null) {
            user.setFullName(dto.getFullName());
        }

        if (dto.getAddress() != null) {
            user.setAddress(dto.getAddress());
        }

        if (dto.getMobile() != null) {
            user.setMobile(dto.getMobile());
        }
        userRepository.save(user);

        return ResponseEntity.ok(
                new MessageResponse(
                        true,
                        HttpStatus.OK,
                        "User updated successfully",
                        user
                )
        );
    }

    @Override
    public ResponseEntity<MessageResponse> createByAdmin(User request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse<>(false, HttpStatus.BAD_REQUEST, "Email already exists", null));
        }

        request.setRole(request.getRole());
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        request.setDeleted(false);

        userRepository.save(request);

        return ResponseEntity.ok(new MessageResponse(true,HttpStatus.OK,"Admin created successfully",request));
    }

    @Override
    public ResponseEntity<MessageResponse> getOneAdmin(Integer id) {

        User admin = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // Check if user is ADMIN
        if (!"ADMIN".equals(admin.getRole())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse(
                            false,
                            HttpStatus.BAD_REQUEST,
                            "User is not an admin",
                            null
                    ));
        }

        return ResponseEntity.ok(
                new MessageResponse(
                        true,
                        HttpStatus.OK,
                        "Admin fetched successfully",
                        admin
                )
        );
    }

    @Override
    public ResponseEntity<MessageResponse> updateByAdmin(Integer id, User request) {

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse(false,HttpStatus.BAD_REQUEST,"Admin not found",null));
        }

        //  Full Name Update
        if (request.getFullName() != null && !request.getFullName().isEmpty()) {
            user.setFullName(request.getFullName());
        }

        //  Email Update
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            user.setEmail(request.getEmail());
        }

        //  Mobile Update
        if (request.getMobile() != null && !request.getMobile().isEmpty()) {
            user.setMobile(request.getMobile());
        }

        // Address Update
        if (request.getAddress() != null && !request.getAddress().isEmpty()) {
            user.setAddress(request.getAddress());
        }
//
//        // Password Update (encoded)
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }


        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse(true,HttpStatus.OK,"Admin updated successfully",user));
    }
//
//    @Override
//    public ResponseEntity<List<User>> getAllCustomersWithReceivedOrders() {
//
//        List<Orders> orders = ordersRepository.findByStatus("RECEIVED");
//
//        List<User> customers = orders.stream()
//                .map(Orders::getUser)
//                .distinct()
//                .toList();
//
//        return ResponseEntity.ok(customers);
//    }
@Override
public ResponseEntity<List<User>> getAllCustomersWithReceivedOrders() {

    List<Orders> orders = ordersRepository.findByStatusNot("CANCELLED");

    List<User> customers = orders.stream()
            .map(Orders::getUser)
            .distinct()
            .toList();

    return ResponseEntity.ok(customers);
}
    @Override
    public ResponseEntity<MessageResponse> forgotPassword(ForgotPasswordDTO request) {

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse<>(false, HttpStatus.NOT_FOUND, "User not found", null));
        }

        User user = optionalUser.get();

        String otp = generateOtp();

        otpStore.put(user.getEmail(), otp);
        otpExpiryStore.put(user.getEmail(), LocalDateTime.now().plusMinutes(5));

        sendOtpEmail(user.getEmail(), otp);

        return ResponseEntity.ok(new MessageResponse(true, HttpStatus.OK,
                "Password reset OTP sent to email", request.getEmail()));
    }

    @Override
    public MessageResponse softDeleteAdmin(Integer adminId) {

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        // Check role
        if (!"ADMIN".equalsIgnoreCase(admin.getRole().name())) {
            throw new DuplicateResourceException("User is not an admin");
        }

        // Prevent deleting Super Admin
        if ("admin@gmail.com".equalsIgnoreCase(admin.getEmail())) {
            throw new DuplicateResourceException("Super Admin cannot be deleted");
        }

        if (admin.isDeleted()) {
            throw new DuplicateResourceException("Admin already deleted");
        }

        admin.setDeleted(true);
        userRepository.save(admin);

        return new MessageResponse(false, HttpStatus.OK, "Admin soft deleted successfully", admin);
    }
    @Override
    public ResponseEntity<MessageResponse> restoreAdmin(Integer adminId) {

        // ✅ Get logged-in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔐 Only SUPER_ADMIN can restore
        if (!"SUPER_ADMIN".equalsIgnoreCase(loggedInUser.getRole().name())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse<>(
                            false,
                            HttpStatus.FORBIDDEN,
                            "Only SUPER_ADMIN can restore admin",
                            null
                    ));
        }

        // ✅ Find soft deleted admin
        User admin = userRepository.findByIdAndDeletedTrue(adminId)
                .orElseThrow(() -> new RuntimeException("Deleted admin not found"));

        admin.setDeleted(false);

        userRepository.save(admin);

        return ResponseEntity.ok(
                new MessageResponse<>(
                        true,
                        HttpStatus.OK,
                        "Admin restored successfully",
                        null
                )
        );
    }
    @Override
    public ResponseEntity<MessageResponse<List<User>>> getActiveAdmins() {

        List<User> admins = userRepository.findByRoleAndDeletedFalse("ADMIN");

        if (admins.isEmpty()) {
            return ResponseEntity.ok(
                    new MessageResponse<>(
                            false,
                            HttpStatus.NO_CONTENT,
                            "No active admins found",
                            null
                    )
            );
        }

        return ResponseEntity.ok(
                new MessageResponse<>(
                        true,
                        HttpStatus.OK,
                        "Active admins fetched successfully",
                        admins
                )
        );
    }
    @Override
    public ResponseEntity<MessageResponse<List<User>>> getDeletedAdmins() {

        List<User> admins = userRepository.findByRoleAndDeletedTrue("ADMIN");

        if (admins.isEmpty()) {
            return ResponseEntity.ok(
                    new MessageResponse<>(
                            false,
                            HttpStatus.NO_CONTENT,
                            "No deleted admins found",
                            null
                    )
            );
        }

        return ResponseEntity.ok(
                new MessageResponse<>(
                        true,
                        HttpStatus.OK,
                        "Deleted admins fetched successfully",
                        admins
                )
        );
    }
    // ================= RESET PASSWORD =================
    @Override
    public MessageResponse resetPassword(ResetPasswordRequestDTO request) {

        String email = request.getEmail();

        if (email == null) {
            return new MessageResponse(false, HttpStatus.BAD_REQUEST,
                    "Email is required", email);
        }

        if (!otpStore.containsKey(email)) {
            return new MessageResponse(false, HttpStatus.BAD_REQUEST,
                    "OTP not found or expired", email);
        }

        if (!otpStore.get(email).equals(request.getOtp())) {
            return new MessageResponse(false, HttpStatus.BAD_REQUEST,
                    "Invalid OTP", email);
        }

        if (otpExpiryStore.get(email).isBefore(LocalDateTime.now())) {
            otpStore.remove(email);
            otpExpiryStore.remove(email);
            return new MessageResponse(false, HttpStatus.BAD_REQUEST,
                    "OTP expired", email);
        }

//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            return new MessageResponse(false, HttpStatus.NOT_FOUND,
                    "User not found", null);
        }

        User user = optionalUser.get();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        otpStore.remove(email);
        otpExpiryStore.remove(email);

        return new MessageResponse(true, HttpStatus.OK,
                "Password reset successfully", email);
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
