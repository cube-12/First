package com.example.librarysystem.service;

import com.example.librarysystem.entity.Book;
import com.example.librarysystem.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> searchBooks(String keyword) {
        return bookRepository.multiFieldSearch(keyword);
    }

    public List<Book> searchByField(String field, String value) {
        if (value == null || value.isEmpty()) return bookRepository.findAll();
        return switch (field) {
            case "title" -> bookRepository.findByTitleContainingIgnoreCase(value);
            case "author" -> bookRepository.findByAuthorContainingIgnoreCase(value);
            case "isbn" -> bookRepository.findByIsbnContainingIgnoreCase(value);
            case "category" -> bookRepository.findByCategoryContainingIgnoreCase(value);
            case "publisher" -> bookRepository.findByPublisherContainingIgnoreCase(value);
            case "callNumber" -> bookRepository.findByCallNumberContainingIgnoreCase(value);
            default -> bookRepository.multiFieldSearch(value);
        };
    }

    public List<Book> getNewBooks() {
        return bookRepository.findTop10ByOrderByCreatedAtDesc();
    }

    public List<Book> getPopularBooks() {
        return bookRepository.findPopularBooks();
    }

    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findByCategoryContainingIgnoreCase(category);
    }
}
