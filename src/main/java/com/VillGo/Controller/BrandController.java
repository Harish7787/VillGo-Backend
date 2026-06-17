package com.VillGo.Controller;

import com.VillGo.DTO.ActionChangeRequest;
import com.VillGo.DTO.BrandRequestDTO;
import com.VillGo.DTO.UpdateDTO.BrandUpdateDTO;
import com.VillGo.DTO.Responce.BrandResponse;
import com.VillGo.DTO.Responce.DataResponse;
import com.VillGo.DTO.Responce.MessageResponse;
import com.VillGo.Service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping(consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> create(@Valid @ModelAttribute BrandRequestDTO dto) {
        return brandService.create(dto);
    }

    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> update(@Valid @PathVariable Integer id, @ModelAttribute BrandUpdateDTO dto) {
        return brandService.update(id, dto);
    }

    @GetMapping("/getone/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<DataResponse<BrandResponse>> getById(@PathVariable Integer id) {
        return brandService.getById(id);
    }

    @PutMapping("/action/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> actionChange(@PathVariable Integer id ,@RequestBody ActionChangeRequest request) {
        return brandService.actionChange(id ,request);
    }
    @DeleteMapping("/soft-delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<MessageResponse<?>> softDelete(@PathVariable Integer id) {
        return ResponseEntity.ok(brandService.softDeleteBrand(id));
    }

    @PutMapping("/restore/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<MessageResponse<?>> restore(@PathVariable Integer id) {
        return ResponseEntity.ok(brandService.restoreBrand(id));
    }
    @GetMapping("/deleted")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<List<BrandResponse>> getDeletedBrands() {
        List<BrandResponse> deletedBrands = brandService.getAllDeletedBrands();
        return ResponseEntity.ok(deletedBrands);
    }
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<List<BrandResponse>> getAllActiveBrands() {
        return ResponseEntity.ok(brandService.getAllActiveBrands());
    }
}
