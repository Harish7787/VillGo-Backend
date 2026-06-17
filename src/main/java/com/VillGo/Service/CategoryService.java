package com.VillGo.Service;

import com.VillGo.DTO.ActionChangeRequest;
import com.VillGo.DTO.CategoryRequestDTO;
import com.VillGo.DTO.Responce.CategoryResponse;
import com.VillGo.DTO.Responce.DataResponse;
import com.VillGo.DTO.Responce.ListResponse;
import com.VillGo.DTO.Responce.MessageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {

    ResponseEntity<MessageResponse> create(CategoryRequestDTO dto);

    ResponseEntity<MessageResponse> update(Integer id, CategoryRequestDTO dto);

    ResponseEntity<ListResponse<CategoryResponse>> getAll();
    ResponseEntity<DataResponse<CategoryResponse>>  getone(Integer id);
    ResponseEntity<MessageResponse> restoreCategory(Integer id);
    ResponseEntity<MessageResponse> deleteCategory(Integer id);
    ResponseEntity<MessageResponse> actionChange(Integer id , ActionChangeRequest request);
    List<CategoryResponse> getAllActiveCategories();
    List<CategoryResponse> getAllDeletedCategories();
}
