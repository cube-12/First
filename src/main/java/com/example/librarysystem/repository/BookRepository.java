package com.example.librarysystem.repository;
import com.example.librarysystem.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByIsbnContainingIgnoreCase(String isbn);
    List<Book> findByCategoryContainingIgnoreCase(String category);
    List<Book> findByPublisherContainingIgnoreCase(String publisher);
    List<Book> findByCallNumberContainingIgnoreCase(String callNumber);

    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String keyword, String keyword1);

    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
           "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
           "LOWER(b.category) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
           "LOWER(b.publisher) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
           "LOWER(b.callNumber) LIKE LOWER(CONCAT('%', :kw, '%'))")
    List<Book> multiFieldSearch(@Param("kw") String keyword);

    List<Book> findTop10ByOrderByCreatedAtDesc();

    @Query("SELECT b FROM Book b ORDER BY (b.quantity - b.available) DESC")
    List<Book> findPopularBooks();
}
