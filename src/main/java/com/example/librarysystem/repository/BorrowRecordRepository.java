package com.example.librarysystem.repository;

import com.example.librarysystem.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    long countByUserId(Long userId);

    long countByUserIdAndStatus(Long userId, String status);

    long countByUserIdAndStatusAndDueDateBefore(
            Long userId, String status, LocalDate dueDate);

    @Query("SELECT br.id, b.id, b.title, b.author, br.borrowDate, br.returnDate, br.dueDate, br.status " +
            "FROM BorrowRecord br JOIN br.book b " +
            "WHERE br.user.id = :userId")
    List<Object[]> findHistoryByUserId(@Param("userId") Long userId);

    @Query("SELECT br.id, b.id, b.title, b.author, br.borrowDate, br.returnDate, br.dueDate, br.status " +
            "FROM BorrowRecord br JOIN br.book b " +
            "WHERE br.user.id = :userId AND br.status = :status")
    List<Object[]> findHistoryByUserIdAndStatus(
            @Param("userId") Long userId,
            @Param("status") String status);

    @Query("SELECT br.id, b.id, b.title, b.author, br.borrowDate, br.returnDate, br.dueDate, br.status " +
            "FROM BorrowRecord br JOIN br.book b " +
            "WHERE br.user.id = :userId AND b.title LIKE %:search%")
    List<Object[]> findHistoryByUserIdAndBookTitle(
            @Param("userId") Long userId,
            @Param("search") String search);

    void deleteByUserId(Long userId);
}