package com.VillGo.Service.Impl;

import com.VillGo.DTO.ProductActionDTO;
import com.VillGo.DTO.ProductRequestDTO;
import com.VillGo.DTO.Responce.DataResponse;
import com.VillGo.DTO.Responce.MessageResponse;
import com.VillGo.DTO.Responce.ProductResponse;
import com.VillGo.Entity.*;
import com.VillGo.Repository.BrandRepository;
import com.VillGo.Repository.CategoryRepository;
import com.VillGo.Repository.ProductRepository;
import com.VillGo.Repository.UserRepository;
import com.VillGo.Service.ImageService;
import com.VillGo.Service.ProductService;
import com.VillGo.Service.Util.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.VillGo.Entity.Product;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ImageService imageService;
    private final UserRepository userRepository;

//    @Override
//    public ResponseEntity<MessageResponse> create(ProductRequestDTO dto) {
//        if (productRepository
//                .existsByNameIgnoreCaseAndBrand_IdAndCategory_IdAndIsDeletedFalse(
//                        dto.getName(),
//                        dto.getBrandId(),      //  correct
//                        dto.getCategoryId()
//                )) {
//
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse<>(
//                            false,
//                            HttpStatus.BAD_REQUEST,
//                            "Product already exists for this brand and category",
//                            null
//                    ));
//
//        }
//
//        Category category = categoryRepository.findById(dto.getCategoryId())
//                .orElseThrow(() -> new RuntimeException("Category not found"));
//
//        Brand brand = brandRepository.findById(dto.getBrandId())
//                .orElseThrow(() -> new RuntimeException("Brand not found"));
//
//        try {
//
//            MessageResponse imageResponse = imageService.uploadImage(dto.getImage());
//
//            if (!imageResponse.isSuccess()) {
//                return ResponseEntity.status(imageResponse.getStatus())
//                        .body(new MessageResponse<>(
//                                false,
//                                imageResponse.getStatus(),
//                                imageResponse.getMessage(),
//                                null
//                        ));
//            }
//
//            String imageUrl = imageResponse.getData().toString();
//
//            Product product = new Product();
//            product.setName(dto.getName());
//            product.setDescription(dto.getDescription());
//            product.setPrice(dto.getPrice());
//            product.setQuantity(dto.getQuantity());
//            product.setImage(imageUrl);   // save URL
//            product.setAction(dto.getAction());
//            product.setCategory(category);
//            product.setBrand(brand);
//
//            productRepository.save(product);
//            return ResponseEntity.ok(
//                    new MessageResponse(true,
//                            HttpStatus.OK,
//                            "Product created successfully",
//                            product)
//            );
//
//        } catch (Exception e) {
//            throw new RuntimeException("Error creating product: " + e.getMessage());
//        }
//    }
@Override
public ResponseEntity<byte[]> downloadProductsPdf() {

    try {

        List<Product> productList = productRepository.findAll();

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        // Title
        Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
        Paragraph title = new Paragraph("E-Commerce Products Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Table
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);

        PdfPCell cell;

        cell = new PdfPCell(new Phrase("ID", headerFont));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Name", headerFont));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Category", headerFont));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Brand", headerFont));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Price", headerFont));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Quantity", headerFont));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        // Table Data
        for (Product product : productList) {

            table.addCell(String.valueOf(product.getId()));
            table.addCell(product.getName());
            table.addCell(product.getCategory().getName());
            table.addCell(product.getBrand().getName());
            table.addCell(String.valueOf(product.getPrice()));
            table.addCell(String.valueOf(product.getQuantity()));
        }

        document.add(table);
        document.close();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=products.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(out.toByteArray());

    } catch (Exception e) {
        throw new RuntimeException("Error generating Product PDF", e);
    }
}
@Override
public ResponseEntity<MessageResponse> create(ProductRequestDTO dto) {

    if (productRepository
            .existsByNameIgnoreCaseAndBrand_IdAndCategory_IdAndIsDeletedFalse(
                    dto.getName(),
                    dto.getBrandId(),
                    dto.getCategoryId()
            )) {

        return ResponseEntity
                .badRequest()
                .body(new MessageResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Product already exists for this brand and category",
                        null
                ));
    }

    Category category = categoryRepository.findById(dto.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found"));

    Brand brand = brandRepository.findById(dto.getBrandId())
            .orElseThrow(() -> new RuntimeException("Brand not found"));

    // ✅ Get Logged-in Admin
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String email = auth.getName();

    User admin = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Admin not found"));

    try {

        MessageResponse imageResponse = imageService.uploadImage(dto.getImage());

        if (!imageResponse.isSuccess()) {
            return ResponseEntity.status(imageResponse.getStatus())
                    .body(new MessageResponse<>(
                            false,
                            imageResponse.getStatus(),
                            imageResponse.getMessage(),
                            null
                    ));
        }

        String imageUrl = imageResponse.getData().toString();

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setImage(imageUrl);
        product.setAction(dto.getAction());
        product.setCategory(category);
        product.setBrand(brand);

        // 🔥 VERY IMPORTANT
        product.setAdmin(admin);
        product.setIsDeleted(false);

        productRepository.save(product);

        return ResponseEntity.ok(
                new MessageResponse<>(
                        true,
                        HttpStatus.OK,
                        "Product created successfully",
                        product)
        );

    } catch (Exception e) {
        throw new RuntimeException("Error creating product: " + e.getMessage());
    }
}
//    @Override
//    public ResponseEntity<DataResponse<List<ProductResponse>>> getAll() {
//        List<ProductResponse> products = productRepository.findAll()
//                .stream()
//                .map(AppUtil::mapToProductResponse)
//                .toList(); // or collect(Collectors.toList());
//
//        return ResponseEntity.ok(
//                new DataResponse<>(true, HttpStatus.OK, "All products fetched", products)
//        );
//    }
@Override
public ResponseEntity<DataResponse<List<ProductResponse>>> getAll() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String email = auth.getName();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    List<Product> products;

    //  SUPER ADMIN sees everything
    if (user.getRole().equals("SUPER_ADMIN")) {
        products = productRepository.findByIsDeletedFalse();
    }
    //  ADMIN sees only his products
    else {
        products = productRepository.findByAdminAndIsDeletedFalse(user);
    }

    List<ProductResponse> response = products.stream()
            .map(AppUtil::mapToProductResponse)
            .toList();

    return ResponseEntity.ok(
            new DataResponse<>(true, HttpStatus.OK, "Products fetched", response)
    );
}
    @Override
    public ResponseEntity<DataResponse<ProductResponse>> getOne(Integer id) {

        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new DataResponse<>(false, HttpStatus.NOT_FOUND, "Product not found", null));
        }
        ProductResponse response = AppUtil.mapToProductResponse(optionalProduct.get());

        return ResponseEntity.ok(
                new DataResponse<>(true, HttpStatus.OK, "Product fetched successfully", response)
        );
    }

    @Override
    public ResponseEntity<MessageResponse> update(Integer id, ProductRequestDTO dto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

// Get final values (new or old)
        String name = dto.getName() != null ? dto.getName() : product.getName();
        Integer brandId = dto.getBrandId() != null ? dto.getBrandId() : product.getBrand().getId();
        Integer categoryId = dto.getCategoryId() != null ? dto.getCategoryId() : product.getCategory().getId();

// Duplicate check excluding current product
        if (productRepository
                .existsByNameIgnoreCaseAndBrand_IdAndCategory_IdAndIdNotAndIsDeletedFalse(
                        name,
                        brandId,
                        categoryId,
                        id
                )) {

            return ResponseEntity.badRequest().body(
                    new MessageResponse<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            "Product already exists for this brand and category",
                            null
                    )
            );
        }

        if (dto.getName() != null) {
            product.setName(dto.getName());
        }

        if (dto.getDescription() != null) {
            product.setDescription(dto.getDescription());
        }

        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }

        if (dto.getQuantity() != null) {
            product.setQuantity(dto.getQuantity());
        }

        if (dto.getAction() != null) {
            product.setAction(dto.getAction());
        }

        //  Update image properly
        if (dto.getImage() != null && !dto.getImage().isEmpty()) {

            MessageResponse imageResponse = imageService.uploadImage(dto.getImage());

            if (!imageResponse.isSuccess()) {
                return ResponseEntity.status(imageResponse.getStatus())
                        .body(new MessageResponse<>(
                                false,
                                imageResponse.getStatus(),
                                imageResponse.getMessage(),
                                null
                        ));
            }

            String imageUrl = imageResponse.getData().toString();

            product.setImage(imageUrl);
        }

        //  Update category
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }

        //  Update brand
        if (dto.getBrandId() != null) {
            Brand brand = brandRepository.findById(dto.getBrandId())
                    .orElseThrow(() -> new RuntimeException("Brand not found"));
            product.setBrand(brand);
        }

        productRepository.save(product);

        return ResponseEntity.ok(
                new MessageResponse(true,
                        HttpStatus.OK,
                        "Product updated successfully",
                        product)
        );
    }
    @Override
    public ResponseEntity<MessageResponse> changeProductStatus(Integer id, ProductActionDTO dto) {

        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse<>(
                            false,
                            HttpStatus.NOT_FOUND,
                            "Product not found with id: " + id,
                            null
                    ));
        }

        Product product = optionalProduct.get();

        if (Boolean.TRUE.equals(product.getIsDeleted())) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            "Cannot change status of deleted product",
                            null
                    )
            );
        }

        if (dto.getAction() == null) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            "Action value is required",
                            null
                    )
            );
        }

        product.setAction(dto.getAction());
        productRepository.save(product);

        String message = dto.getAction() ?
                "Product activated successfully" :
                "Product deactivated successfully";

        return ResponseEntity.ok(
                new MessageResponse<>(
                        true,
                        HttpStatus.OK,
                        message,
                        product
                )
        );
    }
    @Override
    public ResponseEntity<MessageResponse> restore(Integer id) {

        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse<>(
                            false,
                            HttpStatus.NOT_FOUND,
                            "Product not found with id: " + id,
                            null
                    ));
        }

        Product product = optionalProduct.get();

        // If product is not deleted
        if (Boolean.FALSE.equals(product.getIsDeleted())) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            "Product is not deleted",
                            null
                    )
            );
        }

        // Check duplicate before restore
        boolean duplicateExists = productRepository
                .existsByNameIgnoreCaseAndBrand_IdAndCategory_IdAndIsDeletedFalse(
                        product.getName(),
                        product.getBrand().getId(),
                        product.getCategory().getId()
                );

        if (duplicateExists) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            "Cannot restore. Active product with same name already exists.",
                            null
                    )
            );
        }

        product.setIsDeleted(false);
        productRepository.save(product);

        return ResponseEntity.ok(
                new MessageResponse<>(
                        true,
                        HttpStatus.OK,
                        "Product restored successfully",
                        product
                )
        );
    }

    @Override
    public ResponseEntity<MessageResponse> delete(Integer id) {

        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse<>(
                            false,
                            HttpStatus.NOT_FOUND,
                            "Product not found with id: " + id,
                            null
                    ));
        }

        Product product = optionalProduct.get();

        //  If already deleted
        if (Boolean.TRUE.equals(product.getIsDeleted())) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            "Product is already deleted",
                            null
                    )
            );
        }

        //  If product is active
        if (Boolean.TRUE.equals(product.getAction())) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            "Active product cannot be deleted. Please deactivate first.",
                            null
                    )
            );
        }

        //  Soft delete
        product.setIsDeleted(true);
        productRepository.save(product);

        return ResponseEntity.ok(
                new MessageResponse<>(
                        true,
                        HttpStatus.OK,
                        "Product deleted successfully",
                        null
                )
        );
    }
    @Override
    public ResponseEntity<DataResponse<List<ProductResponse>>> getAllActive() {

        List<Product> products = productRepository.findAllByIsDeletedFalse();

        List<ProductResponse> responseList = products.stream()
                .map(AppUtil::mapToProductResponse)
                .toList();

        return ResponseEntity.ok(
                new DataResponse<>(true,HttpStatus.OK, "Active products fetched successfully", responseList)
        );
    }

    @Override
    public ResponseEntity<DataResponse<List<ProductResponse>>> getAllDeleted() {

        List<Product> products = productRepository.findAllByIsDeletedTrue();

        List<ProductResponse> responseList = products.stream()
                .map(AppUtil::mapToProductResponse)
                .toList();

        return ResponseEntity.ok(
                new DataResponse<>(true,HttpStatus.OK, "Deleted products fetched successfully", responseList)
        );
    }
    @Override
    public ResponseEntity<MessageResponse> getMyProducts() {

        // ✅ Get Logged-in User
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // ✅ Fetch Only That Admin Products
        List<Product> products = productRepository
                .findByAdminAndIsDeletedFalse(admin);

        return ResponseEntity.ok(
                new MessageResponse<>(
                        true,
                        HttpStatus.OK,
                        "Products fetched successfully",
                        products
                )
        );
    }
}

