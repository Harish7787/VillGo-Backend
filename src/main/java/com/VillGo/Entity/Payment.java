package com.VillGo.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Table(name = "payments")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentMethod; // CREDIT_CARD

    @NotBlank
    @Pattern(regexp = "^[0-9]{16}$", message = "Card number must be 16 digits")
    private String cardNumber;

    @NotBlank
    private String cardHolderName;

    @Pattern(regexp = "^(0[1-9]|1[0-2])/[0-9]{2}$", message = "Expiry must be MM/YY")
    private String expiry;

    @Pattern(regexp = "^[0-9]{3}$", message = "CVV must be 3 digits")
    private String cvv;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Orders order;
}