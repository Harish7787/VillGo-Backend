package com.VillGo.Repository;


import com.VillGo.Entity.Brand;
import com.VillGo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    List<Brand> findByActionTrue();
    List<Brand> findAllByIsDeletedTrue();
    List<Brand> findAllByIsDeletedFalse();
    List<Brand> findByActionTrueAndIsDeletedFalse();
    List<Brand> findByCreatedByAndActionTrueAndIsDeletedFalse(User user);
    //Optional<Brand> findByIdAndIsDeletedFalse(int id);
}
