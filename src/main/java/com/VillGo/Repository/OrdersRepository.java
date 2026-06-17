package com.VillGo.Repository;

import com.VillGo.Entity.Orders;
import com.VillGo.Entity.Product;
import com.VillGo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findTop5ByOrderByIdDesc();
    boolean existsByUserAndProduct(User user, Product product);
    List<Orders> findByReviewIsNotNull();
    List<Orders> findByUserId(int userId);
    List<Orders> findByStatus(String status);
    List<Orders> findByStatusNot(String status);
    List<Orders> findByProduct_Admin(User admin);

    List<Orders> findByUser(User user);
}
