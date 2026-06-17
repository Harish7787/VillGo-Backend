package com.VillGo.Entity;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.persistence.Id;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private Double price;

    private Integer quantity;

    private String image;

    private Boolean action;

    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;
}


