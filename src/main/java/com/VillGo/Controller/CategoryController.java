package com.VillGo.Controller;

import com.VillGo.DTO.ActionChangeRequest;
import com.VillGo.DTO.CategoryRequestDTO;
import com.VillGo.DTO.Responce.*;
import com.VillGo.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping( value = "/add",consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<MessageResponse> create(@Valid @ModelAttribute CategoryRequestDTO dto) {
        return categoryService.create(dto);
    }

    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<MessageResponse> update(@Valid @PathVariable Integer id, @ModelAttribute CategoryRequestDTO dto) {
        return categoryService.update(id, dto);
    }

    @GetMapping("/getone/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<DataResponse<CategoryResponse>>  getById(@PathVariable Integer id) {
        return categoryService.getone(id);
    }

    @PutMapping("/action/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> actionChange(@PathVariable Integer id ,@RequestBody ActionChangeRequest request) {
        return categoryService.actionChange(id ,request);
    }
    @DeleteMapping("/softdelete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> deleteCategory(@PathVariable Integer id) {
        return categoryService.deleteCategory(id);
    }

    @PutMapping("/restore/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> restoreCategory(@PathVariable Integer id) {
        return categoryService.restoreCategory(id);
    }
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<List<CategoryResponse>> getAllActiveCategories() {
        return ResponseEntity.ok(categoryService.getAllActiveCategories());
    }

    @GetMapping("/deleted")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<List<CategoryResponse>> getAllDeletedCategories() {
        return ResponseEntity.ok(categoryService.getAllDeletedCategories());
    }
}
