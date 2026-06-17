package com.VillGo.Repository;

import com.VillGo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {
    long countByRole(String role);

    Optional<User> findByEmail(String email);
    Optional<User> findByIdAndDeletedFalse(Integer id);
    Optional<User> findById(Integer id);
    List<User> findByRoleAndDeletedFalse(String role);
    Optional<User> findByIdAndDeletedTrue(Integer id);
    List<User> findByRoleAndDeletedTrue(String role);
}
