package com.VillGo.Service;

import com.VillGo.DTO.ForgotPasswordDTO;
import com.VillGo.DTO.ResetPasswordRequestDTO;
import com.VillGo.DTO.Responce.MessageResponse;
import com.VillGo.DTO.UpdateDTO.UpdateUserDTO;
import com.VillGo.Entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<MessageResponse<List<User>>> getAllAdmins();
    ResponseEntity<MessageResponse> updateUser(Integer id, UpdateUserDTO dto);
    ResponseEntity<MessageResponse> createByAdmin(User request);
    ResponseEntity<MessageResponse> updateByAdmin(Integer id, User request);
    ResponseEntity<List<User>> getAllCustomersWithReceivedOrders();
    ResponseEntity<MessageResponse> forgotPassword(ForgotPasswordDTO request);
    MessageResponse softDeleteAdmin(Integer adminId);
    MessageResponse resetPassword(ResetPasswordRequestDTO request);
    ResponseEntity<MessageResponse> restoreAdmin(Integer adminId);
    ResponseEntity<MessageResponse> getOneAdmin(Integer id);
    ResponseEntity<MessageResponse<List<User>>> getActiveAdmins();
    ResponseEntity<MessageResponse<List<User>>> getDeletedAdmins();
}
