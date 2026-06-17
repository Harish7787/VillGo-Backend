package com.VillGo.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewRequestDTO {
    private String review;
    private LocalDateTime orderDate;
}