package com.VillGo.Controller;


import com.VillGo.DTO.ForgotPasswordDTO;
import com.VillGo.DTO.ResetPasswordRequestDTO;
import com.VillGo.DTO.Responce.MessageResponse;
import com.VillGo.DTO.UpdateDTO.UpdateUserDTO;
import com.VillGo.Entity.User;
import com.VillGo.Service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AdminController {

   // private final AuthService authService;
    private final UserService userService;

    @PutMapping("/updateuser/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> updateUser(@PathVariable Integer id, @Valid @RequestBody UpdateUserDTO dto) {
        return userService.updateUser(id, dto);
    }

    @PostMapping("/admin/create-admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> createAdmin(@Valid @RequestBody User request) {
        return userService.createByAdmin(request);
    }

    @GetMapping("/get-one-admin/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> getOneAdmin(@PathVariable Integer id) {
        return userService.getOneAdmin(id);
    }
    @PutMapping("/admin/update/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> updateAdmin(@PathVariable Integer id, @RequestBody User request) {
        return userService.updateByAdmin(id, request);
    }


    @PostMapping("/forgotpassword")
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> forgotPassword(@RequestBody ForgotPasswordDTO request) {
        return userService.forgotPassword(request);
    }

    @PostMapping("/resepassword")
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    public MessageResponse resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        return userService.resetPassword(request);
    }
    @PutMapping("/admin/soft-delete/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> softDeleteAdmin(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.softDeleteAdmin(id));
    }
    @PutMapping("/restore-admin/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> restoreAdmin(@PathVariable Integer id) {
        return userService.restoreAdmin(id);
    }
    @GetMapping("/admin/active")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MessageResponse<List<User>>> getActiveAdmins() {
        return userService.getActiveAdmins();
    }
    @GetMapping("/admin/deleted")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MessageResponse<List<User>>> getDeletedAdmins() {
        return userService.getDeletedAdmins();
    }
}
