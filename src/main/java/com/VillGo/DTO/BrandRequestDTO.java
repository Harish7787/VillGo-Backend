package com.VillGo.DTO;

import com.VillGo.Entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BrandRequestDTO {
    @NotBlank(message = "Brand name is required")
    @Size(min = 2, max = 50, message = "Brand name must be between 2 and 50 characters")
    private String name;

    @NotNull(message = "Brand image is required")
    private MultipartFile image;

    @NotNull(message = "Category ID is required")
    private Integer categoryId;
    private User createdBy;
}
