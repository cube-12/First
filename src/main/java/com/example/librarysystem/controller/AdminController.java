package com.example.librarysystem.controller;

import com.example.librarysystem.entity.Book;
import com.example.librarysystem.entity.PurchaseRecommendation;
import com.example.librarysystem.entity.User;
import com.example.librarysystem.repository.PurchaseRecommendationRepository;
import com.example.librarysystem.repository.UserRepository;
import com.example.librarysystem.service.BookService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private final BookService bookService;
    private final UserRepository userRepository;
    private final PurchaseRecommendationRepository recommendationRepository;

    public AdminController(BookService bookService, UserRepository userRepository,
                           PurchaseRecommendationRepository recommendationRepository) {
        this.bookService = bookService;
        this.userRepository = userRepository;
        this.recommendationRepository = recommendationRepository;
    }

    // ===== 图书管理 =====
    @GetMapping("/books")
    public String bookManagement(@RequestParam(required = false) String keyword,
                                 Authentication auth, Model model) {
        model.addAttribute("username", auth.getName());
        model.addAttribute("currentPage", "admin-books");
        List<Book> books = (keyword != null && !keyword.isEmpty())
                ? bookService.searchBooks(keyword) : bookService.findAllBooks();
        model.addAttribute("books", books);
        model.addAttribute("keyword", keyword);
        return "admin/books";
    }

    @GetMapping("/books/add")
    public String showAddForm(Model model) {
        model.addAttribute("currentPage", "admin-addbook");
        model.addAttribute("book", new Book());
        return "admin/add-book";
    }

    @PostMapping("/books")
    public String addBook(@ModelAttribute Book book) {
        if (book.getAvailable() == 0 && book.getQuantity() > 0) book.setAvailable(book.getQuantity());
        bookService.saveBook(book);
        return "redirect:/admin/books";
    }

    @GetMapping("/books/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("currentPage", "admin-books");
        model.addAttribute("book", bookService.findBookById(id).orElseThrow());
        return "admin/edit-book";
    }

    @PostMapping("/books/update/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book book) {
        Book existing = bookService.findBookById(id).orElseThrow();
        existing.setTitle(book.getTitle());
        existing.setAuthor(book.getAuthor());
        existing.setIsbn(book.getIsbn());
        existing.setPublicationYear(book.getPublicationYear());
        existing.setCategory(book.getCategory());
        existing.setPublisher(book.getPublisher());
        existing.setPublishDate(book.getPublishDate());
        existing.setPages(book.getPages());
        existing.setLanguage(book.getLanguage());
        existing.setCallNumber(book.getCallNumber());
        existing.setDescription(book.getDescription());
        int diff = book.getQuantity() - existing.getQuantity();
        existing.setQuantity(book.getQuantity());
        existing.setAvailable(existing.getAvailable() + diff);
        if (existing.getAvailable() < 0) existing.setAvailable(0);
        if (existing.getAvailable() > book.getQuantity()) existing.setAvailable(book.getQuantity());
        bookService.saveBook(existing);
        return "redirect:/admin/books";
    }

    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/admin/books";
    }

    // ===== 用户管理 =====
    @GetMapping("/users")
    public String userManagement(Authentication auth, Model model) {
        model.addAttribute("username", auth.getName());
        model.addAttribute("currentPage", "admin-users");
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @PostMapping("/users/toggle-role")
    public String toggleRole(@RequestParam Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setRole("ADMIN".equals(user.getRole()) ? "USER" : "ADMIN");
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        model.addAttribute("currentPage", "admin-users");
        model.addAttribute("editUser", userRepository.findById(id).orElseThrow());
        return "admin/edit-user";
    }

    @PostMapping("/users/update")
    public String updateUser(@RequestParam Long userId, @RequestParam String username,
                             @RequestParam String email, @RequestParam String firstName,
                             @RequestParam String lastName, @RequestParam String role) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(role);
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam Long userId) {
        userRepository.deleteById(userId);
        return "redirect:/admin/users";
    }

    // ===== 荐购管理 =====
    @GetMapping("/recommendations")
    public String recommendations(Authentication auth, Model model) {
        model.addAttribute("username", auth.getName());
        model.addAttribute("currentPage", "admin-recs");
        model.addAttribute("recommendations", recommendationRepository.findAllByOrderByCreatedAtDesc());
        model.addAttribute("pendingCount", recommendationRepository.countByStatus("PENDING"));
        return "admin/recommendations";
    }

    @PostMapping("/recommendations/process")
    public String processRecommendation(@RequestParam Long id, @RequestParam String status,
                                        @RequestParam(required = false) String adminReply) {
        PurchaseRecommendation rec = recommendationRepository.findById(id).orElseThrow();
        rec.setStatus(status);
        rec.setAdminReply(adminReply);
        rec.setProcessedAt(LocalDate.now());
        recommendationRepository.save(rec);
        return "redirect:/admin/recommendations";
    }
}
