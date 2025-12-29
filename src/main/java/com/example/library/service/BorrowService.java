package com.example.library.service;

import com.example.library.entity.Book;
import com.example.library.entity.BorrowRecord;
import com.example.library.entity.User;
import com.example.library.repository.BookRepository;
import com.example.library.repository.BorrowRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowService {
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;
    @Autowired
    private BookRepository bookRepository;

    public List<BorrowRecord> findByUser(User user) {
        return borrowRecordRepository.findByUser(user);
    }
    public List<BorrowRecord> findAll() {
        return borrowRecordRepository.findAll();
    }

    public boolean borrowBook(User user, Book book) {
        if (book.getStock() <= 0) return false;
        book.setStock(book.getStock() - 1);
        bookRepository.save(book);

        BorrowRecord record = new BorrowRecord();
        record.setUser(user);
        record.setBook(book);
        record.setBorrowTime(LocalDateTime.now());
        borrowRecordRepository.save(record);
        return true;
    }

    public void returnBook(Long recordId) {
        BorrowRecord record = borrowRecordRepository.findById(recordId).orElse(null);
        if (record != null && record.getReturnTime() == null) {
            record.setReturnTime(LocalDateTime.now());
            Book book = record.getBook();
            book.setStock(book.getStock() + 1);
            bookRepository.save(book);
            borrowRecordRepository.save(record);
        }
    }
}