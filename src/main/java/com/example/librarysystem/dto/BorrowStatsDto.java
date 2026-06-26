package com.example.librarysystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BorrowStatsDto {
    private long totalBorrowed;
    private long returned;
    private long borrowing;
    private long overdue;
}