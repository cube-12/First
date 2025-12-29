package com.example.library.repository;

import com.example.library.entity.BorrowRecord;
import com.example.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByUser(User user);
}