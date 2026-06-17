package com.VillGo.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity = 1;

    private Double totalAmount;

    private String status; // PENDING, PAID, etc

    private String paymentMethod;

    @Column(length = 1000)
    private String review; // customer review

    private LocalDateTime orderDate;
    private LocalDateTime reviewDate;
}
