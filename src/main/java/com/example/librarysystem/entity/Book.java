package com.example.librarysystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Entity
@Table(name = "books")
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(name = "publication_year", nullable = false)
    private int publicationYear;

    @Column(nullable = false)
    private int quantity; // 总数量

    @Column(nullable = false)
    private int available; // 可借数量

    @Column(name = "category")
    private String category;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "publish_date")
    private String publishDate;

    @Column(name = "pages")
    private Integer pages;

    @Column(name = "language", length = 20)
    private String language = "中文";

    @Column(name = "call_number", length = 50)
    private String callNumber;

    @Column(name = "cover_image")
    private String coverImage;

    @Column(columnDefinition = "TEXT")
    private String description;


    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BorrowRecord> borrowRecords = new ArrayList<>();
}
