package com.VillGo.Repository;

import com.VillGo.Entity.Cart;
import com.VillGo.Entity.Product;
import com.VillGo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    List<Cart> findByUser(User user);
    long countByUser_Id(int userId);
    Optional<Cart> findByUserAndProduct(User user, Product product);
}
