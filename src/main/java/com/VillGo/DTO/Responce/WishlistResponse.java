package com.VillGo.DTO.Responce;

import lombok.Data;

@Data
public class WishlistResponse {

    private Integer wishlistId;
    private Integer productId;
    private String productName;
    private String image;
    private Double price;
}