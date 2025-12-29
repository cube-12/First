package com.example.library.controller;

import com.example.library.entity.Book;
import com.example.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // 图书列表
    @GetMapping
    public String list(Model model, @RequestParam(value = "errorMsg", required = false) String errorMsg) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        if (errorMsg != null && !errorMsg.isEmpty()) {
            model.addAttribute("errorMsg", errorMsg);
        }
        return "book_list";
    }

    // 添加图书页
    @GetMapping("/add")
    public String addPage(Model model) {
        model.addAttribute("book", new Book());
        return "book_form";
    }

    // 保存新图书
    @PostMapping("/add")
    public String add(@ModelAttribute Book book) {
        bookService.save(book);
        return "redirect:/books";
    }

    // 编辑图书页
    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        return "book_form";
    }

    // 更新图书
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute Book book) {
        book.setId(id);
        bookService.save(book);
        return "redirect:/books";
    }

    // 删除图书（带外键异常捕获与友好提示）
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        try {
            bookService.deleteById(id);
            return "redirect:/books";
        } catch (DataIntegrityViolationException e) {
            List<Book> books = bookService.findAll();
            model.addAttribute("books", books);
            model.addAttribute("errorMsg", "该图书有借阅记录，无法删除！");
            return "book_list";
        }
    }
}