//package com.E_commerce.Service.Impl;
//
//import com.E_commerce.DTO.CategoryRequestDTO;
//import com.E_commerce.DTO.Responce.CategoryResponse;
//import com.E_commerce.DTO.Responce.MessageResponse;
//import com.E_commerce.Entity.Category;
//import com.E_commerce.Repository.CategoryRepository;
//import com.E_commerce.Service.CategoryService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class CategoryServiceImpl implements CategoryService {
//
//    private final CategoryRepository categoryRepository;
//
//    @Override
//    public ResponseEntity<MessageResponse> create(CategoryRequestDTO dto) {
//
//        Category category = new Category();
//        category.setName(dto.getName());
//        category.setImage(dto.getImage());
//
//        categoryRepository.save(category);
//
//        return ResponseEntity.ok(
//                new MessageResponse(true, HttpStatus.OK, "Category created successfully",category )
//        );
//    }
//
//    @Override
//    public ResponseEntity<MessageResponse> update(Integer id, CategoryRequestDTO dto) {
//
//        Category category = categoryRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Category not found"));
//
//        category.setName(dto.getName());
//        category.setImage(dto.getImage());
//
//        categoryRepository.save(category);
//
//        return ResponseEntity.ok(
//                new MessageResponse(true, HttpStatus.OK , "Category updated successfully",category)
//        );
//    }
//
//    @Override
//    public ResponseEntity<List<CategoryResponse>> getAll() {
//
//        List<CategoryResponse> list = categoryRepository.findAll()
//                .stream()
//                .map(c -> new CategoryResponse(
//                        c.getId(),
//                        c.getName(),
//                        c.getImage(),
//                        c.getAction()
//                ))
//                .toList();
//
//        return ResponseEntity.ok(list);
//    }
//
//    @Override
//    public ResponseEntity<CategoryResponse> getone(Integer id) {
//        Category category = categoryRepository.findById(id)
//                .orElseThrow(() ->
//                        new RuntimeException("Category not found with id: " + id));
//
//        CategoryResponse response = new CategoryResponse();
//        response.setSn(category.getId());
//        response.setName(category.getName());
//        response.setImage(category.getImage());
//        response.setAction(category.getAction());
//
//        return ResponseEntity.ok(response);
//    }
//
//
//    @Override
//    public ResponseEntity<MessageResponse> delete(Integer id) {
//
//        Category category = categoryRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Category not found"));
//
//        category.setAction(false); // soft delete
//        categoryRepository.save(category);
//
//        return ResponseEntity.ok(
//                new MessageResponse(true, HttpStatus.OK , "Category deleted successfully",null)
//        );
//    }
//}
package com.VillGo.Service.Impl;

import com.VillGo.DTO.ActionChangeRequest;
import com.VillGo.DTO.CategoryRequestDTO;
import com.VillGo.DTO.Responce.CategoryResponse;
import com.VillGo.DTO.Responce.DataResponse;
import com.VillGo.DTO.Responce.ListResponse;
import com.VillGo.DTO.Responce.MessageResponse;
import com.VillGo.Entity.Category;
import com.VillGo.Entity.User;
import com.VillGo.Repository.CategoryRepository;
import com.VillGo.Repository.ProductRepository;
import com.VillGo.Repository.UserRepository;
import com.VillGo.Service.CategoryService;
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

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ImageService imageService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
//    @Override
//    public ResponseEntity<MessageResponse> create(CategoryRequestDTO dto) {
//
//        if (categoryRepository.existsByNameIgnoreCase(dto.getName())) {
//            return ResponseEntity.badRequest()
//                    .body(new MessageResponse(false , HttpStatus.BAD_REQUEST,"Already Exist",dto.getName()));
//
//        }
//        MessageResponse imageResponse = imageService.uploadImage(dto.getImage());
//
//        if (!imageResponse.isSuccess()) {
//            return ResponseEntity.status(imageResponse.getStatus())
//                    .body(new MessageResponse<>(
//                            false,
//                            imageResponse.getStatus(),
//                            imageResponse.getMessage(),
//                            null
//                    ));
//        }
//
//// 🔹 Extract Image URL
//        String imageUrl = imageResponse.getData().toString();
//
//// 🔹 Create Category
//        Category category = new Category();
//        category.setName(dto.getName());
//        category.setImage(imageUrl);
//
//        categoryRepository.save(category);
//
//// 🔹 Return Success Response
//        return ResponseEntity.ok(
//                new MessageResponse<>(
//                        true,
//                        HttpStatus.OK,
//                        "Category created successfully",
//                        AppUtil.mapToCategoryResponse(category)
//                )
//        );
//    }

@Override
public ResponseEntity<MessageResponse> create(CategoryRequestDTO dto) {

    if (categoryRepository.existsByNameIgnoreCase(dto.getName())) {
        return ResponseEntity.badRequest()
                .body(new MessageResponse(false ,
                        HttpStatus.BAD_REQUEST,
                        "Already Exist",
                        dto.getName()));
    }

    // ✅ Get logged-in user
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String email = auth.getName();

    User admin = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("admin not found"));

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

    Category category = new Category();
    category.setName(dto.getName());
    category.setImage(imageUrl);

    // 🔥 IMPORTANT LINE
    category.setAdmin(admin);

    categoryRepository.save(category);

    return ResponseEntity.ok(
            new MessageResponse<>(
                    true,
                    HttpStatus.OK,
                    "Category created successfully",
                    AppUtil.mapToCategoryResponse(category)
            )
    );
}
    @Override
    public ResponseEntity<MessageResponse> update(Integer id, CategoryRequestDTO dto) {

        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse<>(false,
                            HttpStatus.NOT_FOUND,
                            "Category not found",
                            null));
        }

        Category category = optionalCategory.get();

        //  Update name only if provided
        if (dto.getName() != null) {
            category.setName(dto.getName());
        }
//        if (categoryRepository.existsByNameIgnoreCase(dto.getName())) {
//            return ResponseEntity.badRequest()
//                    .body(new MessageResponse(false , HttpStatus.BAD_REQUEST,"Category Already Exist",dto.getName()));
//
//        }
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
            category.setImage(imageUrl);
        }
        if (dto.getAction() != null) {
            category.setAction(dto.getAction());
        }
        categoryRepository.save(category);

        return ResponseEntity.ok(
                new MessageResponse<>(true,
                        HttpStatus.OK,
                        "Category updated successfully",
                        category)
        );
    }

//    @Override
//    public ResponseEntity<ListResponse<CategoryResponse>> getAll() {
//
//        List<CategoryResponse> list = categoryRepository.findAll()
//                .stream()
//                .map(AppUtil::mapToCategoryResponse)
//                .toList();
//
//        return ResponseEntity.ok(
//                new ListResponse<>(true,
//                        HttpStatus.OK,
//                        "Category list fetched successfully",
//                        list)
//        );
//    }
@Override
public ResponseEntity<ListResponse<CategoryResponse>> getAll() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String email = auth.getName();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    List<Category> categories;

    // 🔥 SUPER_ADMIN sees all
    if (user.getRole().equals("SUPER_ADMIN")) {
        categories = categoryRepository.findAll();
    }
    // 🔥 ADMIN sees only his categories
    else {
        categories = categoryRepository.findByAdmin(user);
    }

    List<CategoryResponse> list = categories.stream()
            .map(AppUtil::mapToCategoryResponse)
            .toList();

    return ResponseEntity.ok(
            new ListResponse<>(true,
                    HttpStatus.OK,
                    "Category list fetched successfully",
                    list)
    );
}

    @Override
    public ResponseEntity<DataResponse<CategoryResponse>> getone(Integer id) {

        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new DataResponse<>(false, HttpStatus.NOT_FOUND, "Category not found", null));
        }

        CategoryResponse response = AppUtil.mapToCategoryResponse(optionalCategory.get());

        return ResponseEntity.ok(
                new DataResponse<>(true, HttpStatus.OK, "Category fetched successfully", response)
        );
    }

    @Override
    public ResponseEntity<MessageResponse> actionChange(Integer id , ActionChangeRequest request) {

        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse<>(false, HttpStatus.NOT_FOUND, "Category not found", request.getId()));
        }

        Category category = optionalCategory.get();

        category.setAction(request.getAction());
        categoryRepository.save(category);

        return ResponseEntity.ok(
                new MessageResponse<>(true, HttpStatus.OK, "Category Action Changed Successfully", category.getName())
        );
    }
    @Override
    public ResponseEntity<MessageResponse> deleteCategory(Integer id) {

        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(false,
                            HttpStatus.NOT_FOUND,
                            "Category not found",
                            null));
        }

        Category category = optionalCategory.get();

        //  Check if category has products
        if (productRepository.existsByCategoryId(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(false,
                            HttpStatus.BAD_REQUEST,
                            "Cannot delete category. Products exist.",
                            null));
        }

        category.setDeleted(true);  //  Soft delete
        categoryRepository.save(category);

        return ResponseEntity.ok(
                new MessageResponse(true,
                        HttpStatus.OK,
                        "Category deleted successfully",
                        null));
    }
    @Override
    public ResponseEntity<MessageResponse> restoreCategory(Integer id) {

        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(false,
                            HttpStatus.NOT_FOUND,
                            "Category not found",
                            null));
        }

        Category category = optionalCategory.get();

        category.setDeleted(false);   //  Restore
        categoryRepository.save(category);

        return ResponseEntity.ok(
                new MessageResponse(true,
                        HttpStatus.OK,
                        "Category restored successfully",
                        null));
    }
    @Override
    public List<CategoryResponse> getAllActiveCategories() {

        return categoryRepository.findAllByDeletedFalse()
                .stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getImage(),
                        category.getAction()
                ))
                .toList();
    }

    @Override
    public List<CategoryResponse> getAllDeletedCategories() {

        return categoryRepository.findAllByDeletedTrue()
                .stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getImage(),
                        category.getAction()
                ))
                .toList();
    }
}
