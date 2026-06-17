package com.VillGo.Service.Impl;
import com.VillGo.DTO.BuyRequestDTO;
import com.VillGo.DTO.OrderStatusUpdateDTO;
import com.VillGo.DTO.Responce.MessageResponse;
import com.VillGo.DTO.Responce.OrderResponse;
import com.VillGo.DTO.ReviewRequestDTO;
import com.VillGo.Entity.Payment;
import com.VillGo.Entity.Product;
import com.VillGo.Entity.User;
import com.VillGo.Exception.DuplicateResourceException;
import com.VillGo.Exception.ResourceNotFoundException;
import com.VillGo.Repository.OrdersRepository;
import com.VillGo.Repository.PaymentRepository;
import com.VillGo.Repository.ProductRepository;
import com.VillGo.Repository.UserRepository;
import com.VillGo.Service.OrderService;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.VillGo.Entity.Orders;
import org.springframework.transaction.annotation.Transactional;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final PaymentRepository paymentRepository;
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrdersRepository ordersRepository;
    private final Map<Long, String> otpStore = new HashMap<>();
    private final Map<Long, LocalDateTime> otpExpiryStore = new HashMap<>();
    private static final String PENDING = "PENDING";
    private static final String APPROVED = "APPROVED";
    private static final String SHIPPED = "SHIPPED";
    private static final String DELIVERED = "DELIVERED";
    private static final String RECEIVED = "RECEIVED";
    private static final String CANCELLED = "CANCELLED";
    @Override
    public ResponseEntity<MessageResponse> getAllReviews() {

        List<Orders> reviewedOrders = ordersRepository.findByReviewIsNotNull();

        if (reviewedOrders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new MessageResponse(
                            true,
                            HttpStatus.OK,
                            "No reviews found",
                            reviewedOrders
                    ));
        }

        return ResponseEntity.ok(
                new MessageResponse(
                        true,
                        HttpStatus.OK,
                        "All reviews fetched successfully",
                        reviewedOrders
                )
        );
    }

    @Override
    public ResponseEntity<byte[]> downloadOrdersPdf() {

        try {

            List<Orders> ordersList = ordersRepository.findAll();

            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);
            document.open();

            // Title
            Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
            Paragraph title = new Paragraph("E-Commerce Orders Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Table
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);

            Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);

            // Header Cells
            PdfPCell cell;

            cell = new PdfPCell(new Phrase("Order ID", headerFont));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(8);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("User", headerFont));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(8);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Product", headerFont));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(8);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Status", headerFont));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(8);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Price", headerFont));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(8);
            table.addCell(cell);

            // Table Data
            for (Orders order : ordersList) {

                PdfPCell dataCell;

                dataCell = new PdfPCell(new Phrase(String.valueOf(order.getId())));
                dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(dataCell);

                dataCell = new PdfPCell(new Phrase(order.getUser().getFullName()));
                dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(dataCell);

                dataCell = new PdfPCell(new Phrase(order.getProduct().getName()));
                dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(dataCell);

                dataCell = new PdfPCell(new Phrase(order.getStatus()));
                dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(dataCell);

                dataCell = new PdfPCell(new Phrase(String.valueOf(order.getProduct().getPrice())));
                dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(dataCell);
            }

            document.add(table);
            document.close();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=orders.pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
//
//@Override
//@Transactional
//public ResponseEntity<MessageResponse> buy(BuyRequestDTO dto) {
//
//    String email = SecurityContextHolder.getContext().getAuthentication().getName();
//
//    User user = userRepository.findByEmail(email)
//            .orElseThrow(() -> new RuntimeException("User not found"));
//
//    if (user.getAddress() == null || user.getAddress().trim().isEmpty()) {
//        return ResponseEntity.badRequest()
//                .body(new MessageResponse(false,
//                        HttpStatus.BAD_REQUEST,
//                        "Address is required before placing order",
//                        null));
//    }
//
//    Product product = productRepository.findById(dto.getProductId())
//            .orElseThrow(() -> new RuntimeException("Product not found"));
//
//    // Prevent admin buying own product
//    if ("ADMIN".equals(user.getRole()) &&
//            product.getAdmin() != null &&
//            Objects.equals(product.getAdmin().getId(), user.getId())) {
//
//        return ResponseEntity.badRequest()
//                .body(new MessageResponse(false,
//                        HttpStatus.BAD_REQUEST,
//                        "You cannot buy your own product",
//                        null));
//    }
//
//    int quantity = (dto.getQuantity() != null && dto.getQuantity() > 0)
//            ? dto.getQuantity()
//            : 1;
//
//    if (product.getQuantity() < quantity) {
//        return ResponseEntity.badRequest()
//                .body(new MessageResponse(false,
//                        HttpStatus.BAD_REQUEST,
//                        "Not enough stock available",
//                        null));
//    }
//
//    Orders order = Orders.builder()
//            .user(user)
//            .product(product)
//            .quantity(quantity)
//            .totalAmount(product.getPrice() * quantity)
//            .status("PENDING")
//            .paymentMethod(dto.getPaymentMethod())
//            .orderDate(LocalDateTime.now())
//            .build();
//
//    ordersRepository.save(order);
//
//    product.setQuantity(product.getQuantity() - quantity);
//    productRepository.save(product);
//
//    return ResponseEntity.ok(
//            new MessageResponse(true,
//                    HttpStatus.OK,
//                    "Order placed successfully",
//                    product                  )
//    );
//}
@Override
@Transactional
public ResponseEntity<MessageResponse> buy(BuyRequestDTO dto) {

    String email = SecurityContextHolder.getContext().getAuthentication().getName();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (user.getAddress() == null || user.getAddress().trim().isEmpty()) {
        return ResponseEntity.badRequest()
                .body(new MessageResponse(false,
                        HttpStatus.BAD_REQUEST,
                        "Address is required before placing order",
                        null));
    }

    Product product = productRepository.findById(dto.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not found"));

// ADMIN and SUPER_ADMIN cannot buy
    if ("ADMIN".equals(user.getRole()) || "SUPER_ADMIN".equals(user.getRole())) {
        return ResponseEntity.badRequest()
                .body(new MessageResponse(false,
                        HttpStatus.BAD_REQUEST,
                        "Admin and Super Admin cannot buy products",
                        null));
    }

    int quantity = (dto.getQuantity() != null && dto.getQuantity() > 0)
            ? dto.getQuantity()
            : 1;

    if (product.getQuantity() < quantity) {
        return ResponseEntity.badRequest()
                .body(new MessageResponse(false,
                        HttpStatus.BAD_REQUEST,
                        "Not enough stock available",
                        null));
    }

    // PAYMENT VALIDATION
    String paymentMethod = dto.getPaymentMethod();

    if (paymentMethod == null) {
        return ResponseEntity.badRequest()
                .body(new MessageResponse(false,
                        HttpStatus.BAD_REQUEST,
                        "Payment method is required",
                        null));
    }

    switch (paymentMethod.toUpperCase()) {

        case "CREDIT_CARD":

            if (dto.getCardNumber() == null || dto.getCardNumber().length() != 16) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse(false, HttpStatus.BAD_REQUEST,
                                "Invalid card number", null));
            }

            if (dto.getCvv() == null || dto.getCvv().length() != 3) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse(false, HttpStatus.BAD_REQUEST,
                                "Invalid CVV", null));
            }

            if (dto.getExpiry() == null || dto.getExpiry().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse(false, HttpStatus.BAD_REQUEST,
                                "Expiry date required", null));
            }

            break;
//
//        case "UPI":
//
//            if (dto.getUpiId() == null || !dto.getUpiId().contains("@")) {
//                return ResponseEntity.badRequest()
//                        .body(new MessageResponse(false,
//                                HttpStatus.BAD_REQUEST,
//                                "Invalid UPI ID",
//                                null));
//            }
//
//            break;

        case "COD":
            break;

        default:
            return ResponseEntity.badRequest()
                    .body(new MessageResponse(false,
                            HttpStatus.BAD_REQUEST,
                            "Invalid payment method",
                            null));
    }

    // CREATE ORDER
    Orders order = Orders.builder()
            .user(user)
            .product(product)
            .quantity(quantity)
            .totalAmount(product.getPrice() * quantity)
            .status("PENDING")
            .paymentMethod(paymentMethod)
            .orderDate(LocalDateTime.now())
            .build();

    Orders savedOrder = ordersRepository.save(order);

    // UPDATE PRODUCT STOCK
    product.setQuantity(product.getQuantity() - quantity);
    productRepository.save(product);

    // SAVE PAYMENT DETAILS (ONLY FOR CREDIT CARD)
    if (paymentMethod.equalsIgnoreCase("CREDIT_CARD")) {

        Payment payment = new Payment();

        payment.setPaymentMethod(paymentMethod);
        payment.setCardNumber(dto.getCardNumber());
        payment.setCardHolderName(dto.getCardHolderName());
        payment.setExpiry(dto.getExpiry());
        payment.setCvv(dto.getCvv());

        payment.setOrder(savedOrder);

        paymentRepository.save(payment);
    }

    return ResponseEntity.ok(
            new MessageResponse(true,
                    HttpStatus.OK,
                    "Order placed successfully",
                    savedOrder)
    );
}
@Override
public ResponseEntity<MessageResponse> updateOrderStatus(Long       orderId, OrderStatusUpdateDTO dto) {

    Orders order = ordersRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    String newStatus = dto.getStatus().toUpperCase();

    // ================= NORMAL STATUS CHANGE =================
    if (!newStatus.equals(RECEIVED)) {

        order.setStatus(newStatus);
        ordersRepository.save(order);

        // Send status update email
        sendEmail(
                order.getUser().getEmail(),
                "Order Status Updated",
                "Your order #" + orderId + " status changed to: " + newStatus
        );

        // =============== IF DELIVERED → GENERATE OTP ===============
        if (newStatus.equals(DELIVERED)) {

            String otp = String.valueOf(new Random().nextInt(900000) + 100000);

            otpStore.put(orderId, otp);
            otpExpiryStore.put(orderId, LocalDateTime.now().plusMinutes(5));

            sendEmail(
                    order.getUser().getEmail(),
                    "Order Delivery OTP",
                    "Your OTP for confirming delivery is: " + otp +
                            "\nThis OTP is valid for 5 minutes."
            );
        }

        return ResponseEntity.ok(
                new MessageResponse(true, HttpStatus.OK,
                        "Order status updated successfully", newStatus)
        );
    }

    // ================= VERIFY OTP FOR RECEIVED =================
    if (newStatus.equals(RECEIVED)) {

        if (!order.getStatus().equals(DELIVERED)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse(false, HttpStatus.BAD_REQUEST,
                            "Order is not delivered yet", null));
        }

        String enteredOtp = dto.getOtp();

        if (!otpStore.containsKey(orderId)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse(false, HttpStatus.BAD_REQUEST,
                            "OTP not found or expired", null));
        }

        if (otpExpiryStore.get(orderId).isBefore(LocalDateTime.now())) {
            otpStore.remove(orderId);
            otpExpiryStore.remove(orderId);
            return ResponseEntity.badRequest()
                    .body(new MessageResponse(false, HttpStatus.BAD_REQUEST,
                            "OTP expired", null));
        }

        if (!otpStore.get(orderId).equals(enteredOtp)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse(false, HttpStatus.BAD_REQUEST,
                            "Invalid OTP", null));
        }

        // OTP correct → change status
        order.setStatus(RECEIVED);
        ordersRepository.save(order);

        otpStore.remove(orderId);
        otpExpiryStore.remove(orderId);

        sendEmail(
                order.getUser().getEmail(),
                "Order Completed",
                "Your order #" + orderId + " has been successfully received."
        );

        return ResponseEntity.ok(
                new MessageResponse(true, HttpStatus.OK,
                        "Order received successfully", RECEIVED)
        );
    }

    return ResponseEntity.badRequest()
            .body(new MessageResponse(false, HttpStatus.BAD_REQUEST,
                    "Invalid request", null));
}
    private void sendEmail(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("ecommercemart@gmail.com"); // must match application.properties

        mailSender.send(message);
    }

//    @Override
//    public ResponseEntity<MessageResponse> addOrUpdateReview(Long orderId, ReviewRequestDTO dto) {
//        //  Get logged-in user
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // Fetch order
//        Orders order = ordersRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//
//        //  Check ownership
//        if (!Objects.equals(order.getUser().getId(), user.getId()))  {
//            return ResponseEntity.status(403)
//                    .body(new MessageResponse(false,HttpStatus.NOT_FOUND,"You can only review your own orders",orderId));
//        }
//        if (!"RECEIVED".equalsIgnoreCase(order.getStatus().trim())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new MessageResponse(
//                            false,
//                            HttpStatus.BAD_REQUEST,
//                            "You can only review received orders",
//                            orderId
//                    ));
//        }
//        // Update review
//        order.setReview(dto.getReview());
//        ordersRepository.save(order);
//        return ResponseEntity.ok(new MessageResponse(true,HttpStatus.OK,"Review added/updated successfully",dto.getReview()));
//    }
@Override
public MessageResponse cancelOrder(Long orderId) {

    Orders order = ordersRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

    // Already cancelled
    if ("CANCELLED".equalsIgnoreCase(order.getStatus())) {
        throw new DuplicateResourceException("Order already cancelled");
    }

    // Cannot cancel delivered or received
    if ("DELIVERED".equalsIgnoreCase(order.getStatus()) ||
            "RECEIVED".equalsIgnoreCase(order.getStatus())) {
        throw new DuplicateResourceException("Delivered order cannot be cancelled");
    }

    order.setStatus("CANCELLED");

    ordersRepository.save(order);

    return new MessageResponse(false,HttpStatus.OK,"Order cancelled successfully",order);
}
@Override
public ResponseEntity<MessageResponse> addOrUpdateReview(Long orderId, ReviewRequestDTO dto) {

    // Get logged-in user email
    String email = SecurityContextHolder.getContext()
            .getAuthentication()
            .getName();

    Optional<User> optionalUser = userRepository.findByEmail(email);

    if (optionalUser.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponse(
                        false,
                        HttpStatus.NOT_FOUND,
                        "User not found",
                        null
                ));
    }

    User user = optionalUser.get();

    // Fetch order
    Optional<Orders> optionalOrder = ordersRepository.findById(orderId);

    if (optionalOrder.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponse(
                        false,
                        HttpStatus.NOT_FOUND,
                        "Order not found",
                        orderId
                ));
    }

    Orders order = optionalOrder.get();

    // Check ownership
    if (!Objects.equals(order.getUser().getId(), user.getId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new MessageResponse(
                        false,
                        HttpStatus.FORBIDDEN,
                        "You can only review your own orders",
                        orderId
                ));
    }

    // Check status
    if (order.getStatus() == null ||
            !"RECEIVED".equalsIgnoreCase(order.getStatus().trim())) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "You can only review received orders",
                        orderId
                ));
    }

    // Update review
    order.setReview(dto.getReview());
    order.setReviewDate(LocalDateTime.now());
    ordersRepository.save(order);

    return ResponseEntity.ok(
            new MessageResponse(
                    true,
                    HttpStatus.OK,
                    "Review added/updated successfully",
                    dto.getReview()
            )
    );
}
    @Override
    public ResponseEntity<MessageResponse> getOrderById(Long orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        // Build the response
        OrderResponse response = OrderResponse.builder()
                .orderId(order.getId())
                .fullname(order.getUser().getFullName())
                .userEmail(order.getUser().getEmail())
                .productName(order.getProduct().getName())
                .price(order.getProduct().getPrice())
                .quantity(order.getQuantity())
                .totalAmount(order.getProduct().getPrice() * order.getQuantity())
                .paymentMethod(order.getPaymentMethod())
                .status(order.getStatus())
                .review(order.getReview())
                .productImage(order.getProduct().getImage())
                .orderDate(order.getOrderDate()) // Assuming you have createdAt field in Orders
                .build();

        return ResponseEntity.ok(
                new MessageResponse(true,HttpStatus.OK, "Order fetched successfully", response)
        );
    }
@Override
    public ResponseEntity<MessageResponse> getAllOrders() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Orders> orders;

        if ("SUPER_ADMIN".equals(user.getRole())) {

            orders = ordersRepository.findAll();

        } else if ("ADMIN".equals(user.getRole())) {

            orders = ordersRepository.findByProduct_Admin(user);

        } else {

            orders = ordersRepository.findByUser(user);
        }

        List<OrderResponse> response = orders.stream().map(order ->

                OrderResponse.builder()
                        .orderId(order.getId())
                        .fullname(order.getUser().getFullName())
                        .userEmail(order.getUser().getEmail())
                        .productName(order.getProduct().getName())
                        .price(order.getProduct().getPrice())
                        .productImage(order.getProduct().getImage())
                        .quantity(order.getQuantity())
                        .totalAmount(order.getTotalAmount())
                        .paymentMethod(order.getPaymentMethod())
                        .status(order.getStatus())
                        .review(order.getReview())
                        .orderDate(order.getOrderDate())
                        .build()

        ).toList();

        return ResponseEntity.ok(
                new MessageResponse(
                        true,
                        HttpStatus.OK,
                        "Orders fetched successfully",
                        response
                )
        );
    }

    @Override
    public ResponseEntity<MessageResponse> getMyOrders() {

        // Get logged-in user email
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(
                            false,
                            HttpStatus.NOT_FOUND,
                            "User not found",
                            null
                    ));
        }

        User user = optionalUser.get();

        List<Orders> orders = ordersRepository.findByUserId(user.getId());

        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new MessageResponse(
                            true,
                            HttpStatus.OK,
                            "No orders found for this user",
                            orders
                    ));
        }

        return ResponseEntity.ok(
                new MessageResponse(
                        true,
                        HttpStatus.OK,
                        "User orders fetched successfully",
                        orders
                )
        );
    }
}
