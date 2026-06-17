package com.VillGo.DTO.UpdateDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BrandUpdateDTO {

    private String name;
    private MultipartFile image;
    private Integer categoryId;
    private Boolean action;


}
