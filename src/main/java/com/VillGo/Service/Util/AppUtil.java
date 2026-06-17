package com.VillGo.Service.Util;

import com.VillGo.DTO.Responce.BrandResponse;
import com.VillGo.DTO.Responce.CategoryResponse;
import com.VillGo.DTO.Responce.OrderResponse;
import com.VillGo.DTO.Responce.ProductResponse;
import com.VillGo.Entity.Brand;
import com.VillGo.Entity.Category;
import com.VillGo.Entity.Orders;
import com.VillGo.Entity.Product;

public class AppUtil {
    private AppUtil() {}

    public static BrandResponse mapToBrandResponse(Brand brand) {
        return new BrandResponse(
                brand.getId(),
                brand.getName(),
                brand.getImage(),
                brand.getAction(),
                brand.getCategory().getId(),
                brand.getCategory().getName()
        );
    }
    public static CategoryResponse mapToCategoryResponse(Category category) {

        if (category == null) {
            return null;
        }

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getImage(),
                category.getAction()
        );
    }
    public static ProductResponse mapToProductResponse(Product product) {
        if (product == null) return null;

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getImage(),
                product.getPrice(),
                product.getAction(),
                product.getDescription()
        );
    }
    public static OrderResponse mapToOrderResponse(Orders order){

        if(order == null){
            return null;
        }

        return OrderResponse.builder()
                .orderId(order.getId())
                .productName(order.getProduct().getName())
                .price(order.getProduct().getPrice())
                .quantity(order.getQuantity())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .paymentMethod(order.getPaymentMethod())
                .review(order.getReview())
                .orderDate(order.getOrderDate())
                .build();
    }
}
