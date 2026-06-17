package com.VillGo.DTO;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BuyRequestDTO {
    @NotNull(message = "Product ID is required")
    @Min(value = 1, message = "Product ID must be greater than 0")
    private Integer productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    // Credit Card Fields
    @Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")
    private String cardNumber;

    @Size(min = 3, max = 100, message = "Card holder name must be valid")
    private String cardHolderName;

    private String expiry;

    @Pattern(regexp = "\\d{3}", message = "CVV must be 3 digits")
    private String cvv;

    // UPI
    @Email(message = "UPI ID must be valid")
    private String upiId;
}
