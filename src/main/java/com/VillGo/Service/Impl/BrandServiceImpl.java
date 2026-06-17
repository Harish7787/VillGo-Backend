//package com.E_commerce.Service.Impl;
//
//import com.E_commerce.DTO.BrandRequestDTO;
//import com.E_commerce.DTO.Responce.BrandResponse;
//import com.E_commerce.DTO.Responce.MessageResponse;
//import com.E_commerce.Entity.Brand;
//import com.E_commerce.Entity.Category;
//import com.E_commerce.Repository.BrandRepository;
//import com.E_commerce.Repository.CategoryRepository;
//import com.E_commerce.Service.BrandService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class BrandServiceImpl implements BrandService {
//
//    private final BrandRepository brandRepository;
//    private final CategoryRepository categoryRepository;
//
//    @Override
//    public ResponseEntity<MessageResponse> create(BrandRequestDTO dto) {
//
//        Category category = categoryRepository.findById(dto.getCategoryId())
//                .orElseThrow(() -> new RuntimeException("Category not found"));
//
//        Brand brand = new Brand();
//        brand.setName(dto.getName());
//        brand.setImage(dto.getImage());
//        brand.setCategory(category);
//
//        brandRepository.save(brand);
//
//        return ResponseEntity.ok(
//                new MessageResponse(true, HttpStatus.OK , "Brand created successfully",brand)
//        );
//    }
//
//    @Override
//    public ResponseEntity<MessageResponse> update(Integer id, BrandRequestDTO dto) {
//
//        Brand brand = brandRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Brand not found"));
//
//        Category category = categoryRepository.findById(dto.getCategoryId())
//                .orElseThrow(() -> new RuntimeException("Category not found"));
//
//        brand.setName(dto.getName());
//        brand.setImage(dto.getImage());
//        brand.setCategory(category);
//
//        brandRepository.save(brand);
//
//        return ResponseEntity.ok(
//                new MessageResponse(true, HttpStatus.OK,"Brand updated successfully",brand)
//        );
//    }
//
//    @Override
//    public ResponseEntity<List<BrandResponse>> getAll() {
//
//        List<BrandResponse> list = brandRepository.findByActionTrue()
//                .stream()
//                .map(b -> new BrandResponse(
//                        b.getId(),
//                        b.getName(),
//                        b.getImage(),
//                        b.getAction(),
//                        b.getCategory().getId(),
//                        b.getCategory().getName()
//                ))
//                .toList();
//
//        return ResponseEntity.ok(list);
//    }
//
//    @Override
//    public ResponseEntity<BrandResponse> getById(Integer id) {
//
//        Brand brand = brandRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Brand not found"));
//
//        BrandResponse dto = new BrandResponse(
//                brand.getId(),
//                brand.getName(),
//                brand.getImage(),
//                brand.getAction(),
//                brand.getCategory().getId(),
//                brand.getCategory().getName()
//        );
//
//        return ResponseEntity.ok(dto);
//    }
//
//    @Override
//    public ResponseEntity<MessageResponse> delete(Integer id) {
//
//        Brand brand = brandRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Brand not found"));
//
//        brand.setAction(false); // soft delete
//        brandRepository.save(brand);
//
//        return ResponseEntity.ok(
//                new MessageResponse(true, HttpStatus.OK , "Brand deleted successfully",null)
//        );
//    }
//}
package com.VillGo.Service.Impl;

import com.VillGo.DTO.ActionChangeRequest;
import com.VillGo.DTO.BrandRequestDTO;
import com.VillGo.DTO.UpdateDTO.BrandUpdateDTO;
import com.VillGo.DTO.Responce.BrandResponse;
import com.VillGo.DTO.Responce.DataResponse;
import com.VillGo.DTO.Responce.ListResponse;
import com.VillGo.DTO.Responce.MessageResponse;
import com.VillGo.Entity.Brand;
import com.VillGo.Entity.Category;
import com.VillGo.Entity.User;
import com.VillGo.Repository.BrandRepository;
import com.VillGo.Repository.CategoryRepository;
import com.VillGo.Repository.ProductRepository;
import com.VillGo.Repository.UserRepository;
import com.VillGo.Service.BrandService;
import com.VillGo.Service.ImageService;
import com.VillGo.Service.Util.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;
    private final UserRepository userRepository;

//    @Override
//    public ResponseEntity<MessageResponse> create(BrandRequestDTO dto) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String email = auth.getName();
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        dto.setCreatedBy(user);
//        Optional<Category> optionalCategory =
//                categoryRepository.findById(dto.getCategoryId());
//
//        if (optionalCategory.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new MessageResponse<>(false,
//                            HttpStatus.NOT_FOUND,
//                            "Category not found",
//                            dto.getCategoryId()));
//        }
//
//// 🔹 Upload Image Properly
//        MessageResponse imageResponse = imageService.uploadImage(dto.getImage());
//
//        if (!imageResponse.isSuccess()) {
//            return ResponseEntity.status(imageResponse.getStatus())
//                    .body(new MessageResponse<>(false,
//                            imageResponse.getStatus(),
//                            imageResponse.getMessage(),
//                            null));
//        }
//
//        String imageUrl = imageResponse.getData().toString();
//
//// 🔹 Create Brand
//        Brand brand = new Brand();
//        brand.setName(dto.getName());
//        brand.setImage(imageUrl);
//        brand.setCategory(optionalCategory.get());
//
//        brandRepository.save(brand);
//
//// 🔹 Return Success
//        return ResponseEntity.ok(
//                new MessageResponse<>(true,
//                        HttpStatus.OK,
//                        "Brand created successfully",
//                        AppUtil.mapToBrandResponse(brand))
//        );
//
//    }

@Override
public ResponseEntity<MessageResponse> create(BrandRequestDTO dto) {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String email = auth.getName();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    Optional<Category> optionalCategory =
            categoryRepository.findById(dto.getCategoryId());

    if (optionalCategory.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponse<>(false,
                        HttpStatus.NOT_FOUND,
                        "Category not found",
                        dto.getCategoryId()));
    }

    MessageResponse imageResponse = imageService.uploadImage(dto.getImage());

    if (!imageResponse.isSuccess()) {
        return ResponseEntity.status(imageResponse.getStatus())
                .body(new MessageResponse<>(false,
                        imageResponse.getStatus(),
                        imageResponse.getMessage(),
                        null));
    }

    String imageUrl = imageResponse.getData().toString();

    // ✅ Create Brand Properly
    Brand brand = new Brand();
    brand.setName(dto.getName());
    brand.setImage(imageUrl);
    brand.setCategory(optionalCategory.get());

    // 🔥 VERY IMPORTANT LINE
    brand.setCreatedBy(user);

    brandRepository.save(brand);

    return ResponseEntity.ok(
            new MessageResponse<>(true,
                    HttpStatus.OK,
                    "Brand created successfully",
                    AppUtil.mapToBrandResponse(brand))
    );
}
    @Override
    public ResponseEntity<MessageResponse> update(Integer id, BrandUpdateDTO dto) {

        Optional<Brand> optionalBrand = brandRepository.findById(id);

        if (optionalBrand.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse<>(false,
                            HttpStatus.NOT_FOUND,
                            "Brand not found",
                            id));
        }

        Brand brand = optionalBrand.get();

//  Update name
        if (dto.getName() != null) {
            brand.setName(dto.getName());
        }

        //Update Action
        if (dto.getAction() != null) {
            brand.setAction(dto.getAction());
        }

//  Update image properly
        if (dto.getImage() != null && !dto.getImage().isEmpty()) {

            MessageResponse response = imageService.uploadImage(dto.getImage());

            if (!response.isSuccess()) {
                return ResponseEntity.status(response.getStatus())
                        .body(response);  // stop if upload failed
            }

            String imageUrl = response.getData().toString();
            brand.setImage(imageUrl);
        }


//  Update category if provided
        if (dto.getCategoryId() != null) {

            Optional<Category> optionalCategory =
                    categoryRepository.findById(dto.getCategoryId());

            if (optionalCategory.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse<>(false,
                                HttpStatus.NOT_FOUND,
                                "Category id not found",
                                dto.getCategoryId()));
            }

            brand.setCategory(optionalCategory.get());
        }

        brandRepository.save(brand);

        return ResponseEntity.ok(
                new MessageResponse<>(true,
                        HttpStatus.OK,
                        "Brand updated successfully",
                        brand)
        );
    }

//    @Override
//    public ResponseEntity<ListResponse<BrandResponse>> getAll() {
//
//        List<BrandResponse> list = brandRepository.findByActionTrue()
//                .stream()
//                .map(AppUtil::mapToBrandResponse)
//                .toList();
//
//        return ResponseEntity.ok(
//                new ListResponse<>(true, HttpStatus.OK, "Brand list fetched successfully", list)
//        );
//    }
@Override
public ResponseEntity<ListResponse<BrandResponse>> getAll() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String email = auth.getName();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    List<Brand> brands;

    // ✅ SUPER_ADMIN → see all brands
    if ("SUPER_ADMIN".equalsIgnoreCase(user.getRole().name())) {
        brands = brandRepository.findByActionTrueAndIsDeletedFalse();
    }
    // ✅ ADMIN → see only own brands
    else {
        brands = brandRepository.findByCreatedByAndActionTrueAndIsDeletedFalse(user);
    }

    List<BrandResponse> list = brands.stream()
            .map(AppUtil::mapToBrandResponse)
            .toList();

    return ResponseEntity.ok(
            new ListResponse<>(true, HttpStatus.OK, "Brand list fetched successfully", list)
    );
}
    @Override
    public ResponseEntity<DataResponse<BrandResponse>> getById(Integer id) {

        Optional<Brand> optionalBrand = brandRepository.findById(id);

        if (optionalBrand.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new DataResponse<>(false, HttpStatus.NOT_FOUND, "Brand not found", null));
        }

        BrandResponse response = AppUtil.mapToBrandResponse(optionalBrand.get());

        return ResponseEntity.ok(
                new DataResponse<>(true, HttpStatus.OK, "Brand fetched successfully", response)
        );
    }

@Override
public MessageResponse<?> softDeleteBrand(Integer id) {

    Brand brand = brandRepository.findById(id)
            .orElse(null);

    if (brand == null) {
        return new MessageResponse<>(
                false,
                HttpStatus.NOT_FOUND,
                "Brand not found",
                null
        );
    }

    // 1️⃣ Already deleted check
    if (Boolean.TRUE.equals(brand.getIsDeleted())) {
        return new MessageResponse<>(
                false,
                HttpStatus.BAD_REQUEST,
                "Brand already deleted",
                null
        );
    }

    // 2️⃣ Category active check
    if (brand.getCategory() != null &&
            Boolean.FALSE.equals(brand.getCategory().getDeleted())) {

        return new MessageResponse<>(
                false,
                HttpStatus.BAD_REQUEST,
                "Cannot delete brand. Category is still active.",
                null
        );
    }

    // 3️⃣ Active product check
    boolean hasActiveProduct =
            productRepository.existsByBrand_IdAndIsDeletedFalse(id);

    if (hasActiveProduct) {
        return new MessageResponse<>(
                false,
                HttpStatus.BAD_REQUEST,
                "Cannot delete brand. Active products exist under this brand.",
                null
        );
    }

    // 4️⃣ Soft delete brand
    brand.setIsDeleted(true);
    brandRepository.save(brand);

    return new MessageResponse<>(
            true,
            HttpStatus.OK,
            "Brand soft deleted successfully",
            null
    );
}
    @Override
    public MessageResponse<?> restoreBrand(Integer id) {

        Brand brand = brandRepository.findById(id).orElse(null);

        if (brand == null) {
            return new MessageResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    "Brand not found",
                    null
            );
        }

        if (Boolean.FALSE.equals(brand.getIsDeleted())) {
            return new MessageResponse<>(
                    false,
                    HttpStatus.BAD_REQUEST,
                    "Brand is already active",
                    null
            );
        }

        brand.setIsDeleted(false);
        brandRepository.save(brand);

        return new MessageResponse<>(
                true,
                HttpStatus.OK,
                "Brand restored successfully",
                null
        );
    }

    @Override
    public ResponseEntity<MessageResponse> actionChange(Integer id , ActionChangeRequest request) {

        Optional<Brand> optionalBrand = brandRepository.findById(id);

        if (optionalBrand.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse<>(false, HttpStatus.NOT_FOUND, "Brand not found", request.getId()));
        }

        Brand brand = optionalBrand.get();

        brand.setAction(request.getAction()); // dynamic true/false
        brandRepository.save(brand);

        return ResponseEntity.ok(
                new MessageResponse<>(true, HttpStatus.OK, "Brand Action Changed Successfully", brand.getName())
        );
    }
    @Override
    public List<BrandResponse> getAllDeletedBrands() {
        // Fetch all brands where isDeleted = true
        List<Brand> deletedBrands = brandRepository.findAllByIsDeletedTrue();

        // Map Brand entity to BrandResponse DTO
        return deletedBrands.stream().map(brand -> {
            BrandResponse response = new BrandResponse();
            response.setId(brand.getId());
            response.setName(brand.getName());
            response.setImage(brand.getImage());
            response.setAction(brand.getAction());
            if (brand.getCategory() != null) {
                response.setCategoryId(brand.getCategory().getId());
                response.setCategoryName(brand.getCategory().getName());
            }
            return response;
        }).collect(Collectors.toList());
    }
    @Override
    public List<BrandResponse> getAllActiveBrands() {

        List<Brand> brands = brandRepository.findAllByIsDeletedFalse();

        return brands.stream()
                .map(brand -> new BrandResponse(
                        brand.getId(),
                        brand.getName(),
                        brand.getImage(),
                        brand.getAction(),
                        brand.getCategory() != null ? brand.getCategory().getId() : null,
                        brand.getCategory() != null ? brand.getCategory().getName() : null
                ))
                .toList();
    }
}
