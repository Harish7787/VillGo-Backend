package com.VillGo.DTO.Responce;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponse {

    private Long orderId;

    private String fullname;
    private String userEmail;

    private String productName;
    private Double price;

    private Integer quantity;
    private Double totalAmount;

    private String paymentMethod;
    private String status;
    private String review;
    private String productImage;
    private LocalDateTime orderDate;


}