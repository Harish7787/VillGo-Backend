package com.VillGo.Controller;

import com.VillGo.DTO.ProductActionDTO;
import com.VillGo.DTO.ProductRequestDTO;
import com.VillGo.DTO.Responce.DataResponse;
import com.VillGo.DTO.Responce.MessageResponse;
import com.VillGo.DTO.Responce.ProductResponse;
import com.VillGo.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping( value = "/add",consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<MessageResponse> create(@Valid @ModelAttribute ProductRequestDTO dto) {
        return productService.create(dto);
    }
    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<MessageResponse> update(@PathVariable Integer id, @ModelAttribute ProductRequestDTO dto) {
        return productService.update(id, dto);
    }
    @PutMapping("/status/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<MessageResponse> changeStatus(@PathVariable Integer id, @RequestBody ProductActionDTO dto) {
        return productService.changeProductStatus(id, dto);
    }

    @GetMapping("/getone/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<DataResponse<ProductResponse>> getOne(@PathVariable Integer id) {
        return productService.getOne(id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<MessageResponse> delete(@PathVariable Integer id) {
        return productService.delete(id);
    }
    @PutMapping("/restore/{id}")
    public ResponseEntity<MessageResponse> restoreProduct(@PathVariable Integer id) {
        return productService.restore(id);
    }
    @GetMapping("/active")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse<List<ProductResponse>>> getAllActive() {
        return productService.getAllActive();
    }

    @GetMapping("/deleted")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<DataResponse<List<ProductResponse>>> getAllDeleted() {
        return productService.getAllDeleted();
    }
    @GetMapping("/admin-products")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<MessageResponse> getMyProducts() {
        return productService.getMyProducts();
    }

}

