package com.example.librarysystem.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BorrowHistoryDto {
    private Long borrowId;
    private Long bookId;
    private String bookTitle;
    private String bookAuthor;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private LocalDate dueDate;
    private String status;

    public BorrowHistoryDto(Long borrowId, Long bookId, String bookTitle, String bookAuthor,
                            LocalDate borrowDate, LocalDate returnDate,
                            LocalDate dueDate, String status) {
        this.borrowId = borrowId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.dueDate = dueDate;
        this.status = status;
    }
}