package com.VillGo.Repository;

import com.VillGo.Entity.Product;
import com.VillGo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
   boolean existsByBrand_IdAndIsDeletedFalse(Integer id);
    boolean existsByCategoryId(Integer categoryId);
    boolean existsByNameIgnoreCaseAndBrand_IdAndCategory_IdAndIsDeletedFalse(
            String name,
            Integer brandId,
            Integer categoryId
    );
    boolean existsByNameIgnoreCaseAndBrand_IdAndCategory_IdAndIdNotAndIsDeletedFalse(
            String name,
            Integer brandId,
            Integer categoryId,
            Integer id
    );
 List<Product> findAllByIsDeletedFalse();
 List<Product> findAllByIsDeletedTrue();
 List<Product> findByAdmin(User admin);
 List<Product> findByAdminAndIsDeletedFalse(User admin);
 List<Product> findByIsDeletedFalse();

}
