package com.example.librarysystem.repository;

import com.example.librarysystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    // 添加这两个方法
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}