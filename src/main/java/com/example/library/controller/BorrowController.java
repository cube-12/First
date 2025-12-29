package com.example.library.controller;

import com.example.library.entity.Book;
import com.example.library.entity.BorrowRecord;
import com.example.library.entity.User;
import com.example.library.service.BookService;
import com.example.library.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/borrow")
public class BorrowController {
    @Autowired
    private BorrowService borrowService;
    @Autowired
    private BookService bookService;

    @GetMapping
    public String borrowList(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if ("ADMIN".equals(user.getRole())) {
            model.addAttribute("records", borrowService.findAll());
        } else {
            model.addAttribute("records", borrowService.findByUser(user));
        }
        return "borrow_list";
    }

    @GetMapping("/borrow/{bookId}")
    public String borrowBook(@PathVariable Long bookId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Book book = bookService.findById(bookId);
        boolean success = borrowService.borrowBook(user, book);
        if (!success) {
            model.addAttribute("error", "库存不足！");
        }
        return "redirect:/books";
    }

    @GetMapping("/return/{recordId}")
    public String returnBook(@PathVariable Long recordId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        borrowService.returnBook(recordId);
        return "redirect:/borrow";
    }
}