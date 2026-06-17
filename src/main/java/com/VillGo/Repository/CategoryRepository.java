package com.VillGo.Repository;


import com.VillGo.Entity.Category;
import com.VillGo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
  //  boolean existsByBrandIdAndIsDeletedFalse(int id);
  boolean existsByNameIgnoreCase(String name);
  List<Category> findAllByDeletedFalse();
  List<Category> findByAdmin(User admin);
  // Get all deleted
  List<Category> findAllByDeletedTrue();
}
