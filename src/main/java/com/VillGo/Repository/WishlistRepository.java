package com.VillGo.Repository;

import com.VillGo.Entity.User;
import com.VillGo.Entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {

//    List<Wishlist> findByUserId(Integer userId);
    long countByUser_Id(int userId);
    List<Wishlist> findByUser(User user);
    Optional<Wishlist> findByUser_IdAndProduct_Id(Integer userId, Integer productId);
    boolean existsByUserIdAndProductId(Integer userId, Integer productId);
}
