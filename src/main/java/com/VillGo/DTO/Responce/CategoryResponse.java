package com.VillGo.DTO.Responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CategoryResponse{
    private Integer sn;
    private String name;
    private String image;
    private Boolean action;
}
