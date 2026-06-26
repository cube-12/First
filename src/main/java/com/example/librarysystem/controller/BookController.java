package com.example.librarysystem.controller;

import com.example.librarysystem.entity.Book;
import com.example.librarysystem.entity.PurchaseRecommendation;
import com.example.librarysystem.entity.User;
import com.example.librarysystem.repository.PurchaseRecommendationRepository;
import com.example.librarysystem.service.BookService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final PurchaseRecommendationRepository recommendationRepository;

    public BookController(BookService bookService, PurchaseRecommendationRepository recommendationRepository) {
        this.bookService = bookService;
        this.recommendationRepository = recommendationRepository;
    }

    @GetMapping
    public String bookList(@RequestParam(required = false) String field,
                           @RequestParam(required = false) String keyword,
                           @RequestParam(required = false) String category,
                           Authentication auth, Model model) {
        if (auth != null) model.addAttribute("username", auth.getName());
        model.addAttribute("currentPage", "books");

        List<Book> books;
        if (category != null && !category.isEmpty()) {
            books = bookService.getBooksByCategory(category);
            model.addAttribute("currentCategory", category);
        } else if (keyword != null && !keyword.isEmpty()) {
            books = field != null && !field.isEmpty()
                    ? bookService.searchByField(field, keyword)
                    : bookService.searchBooks(keyword);
            model.addAttribute("keyword", keyword);
            model.addAttribute("field", field);
        } else {
            books = bookService.findAllBooks();
        }
        model.addAttribute("books", books);
        model.addAttribute("newBooks", bookService.getNewBooks());
        model.addAttribute("popularBooks", bookService.getPopularBooks());

        List<String> categories = books.stream()
                .map(Book::getCategory).filter(java.util.Objects::nonNull).distinct().sorted().toList();
        model.addAttribute("categories", categories);
        return "books";
    }

    @GetMapping("/detail/{id}")
    public String bookDetail(@PathVariable Long id, Authentication auth, Model model) {
        if (auth != null) model.addAttribute("username", auth.getName());
        model.addAttribute("currentPage", "books");
        model.addAttribute("book", bookService.findBookById(id).orElseThrow());
        return "book-detail";
    }

    @GetMapping("/recommend")
    public String recommendForm(Model model) {
        model.addAttribute("recommendation", new PurchaseRecommendation());
        model.addAttribute("currentPage", "recommend");
        return "book-recommend";
    }

    @PostMapping("/recommend")
    public String submitRecommend(@ModelAttribute PurchaseRecommendation rec, Authentication auth) {
        if (auth != null) {
            User user = (User) auth.getPrincipal();
            rec.setUserId(user.getId());
            rec.setUserName(user.getUsername());
        }
        recommendationRepository.save(rec);
        return "redirect:/books/recommend";
    }

    @GetMapping("/my-recommendations")
    public String myRecommendations(Authentication auth, Model model) {
        model.addAttribute("currentPage", "myrec");
        if (auth != null) {
            User user = (User) auth.getPrincipal();
            model.addAttribute("recommendations",
                    recommendationRepository.findByUserIdOrderByCreatedAtDesc(user.getId()));
        } else {
            model.addAttribute("recommendations", List.of());
        }
        return "my-recommendations";
    }
}
