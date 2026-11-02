package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    // Constructor injection: Spring gives us the BookService object
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // ---------- CRUD ----------

    @PostMapping
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addBook(book));
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Integer id) {
        return bookService.getBookById(id);
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Integer id, @Valid @RequestBody Book book) {
        return bookService.updateBook(id, book);
    }

    // PATCH = change only availability: PATCH /api/books/1/availability?available=false
    @PatchMapping("/{id}/availability")
    public Book updateAvailability(@PathVariable Integer id, @RequestParam Boolean available) {
        return bookService.updateAvailability(id, available);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteBook(@PathVariable Integer id) {
        bookService.deleteBook(id);
        return Map.of("message", "Book with ID " + id + " deleted successfully");
    }

    // ---------- Custom queries ----------

    // GET /api/books/search?keyword=java
    @GetMapping("/search")
    public List<Book> searchByName(@RequestParam String keyword) {
        return bookService.searchByName(keyword);
    }

    // GET /api/books/author?name=Joshua Bloch
    @GetMapping("/author")
    public List<Book> getByAuthor(@RequestParam String name) {
        return bookService.getByAuthor(name);
    }

    // GET /api/books/available
    @GetMapping("/available")
    public List<Book> getAvailableBooks() {
        return bookService.getAvailableBooks();
    }

    // GET /api/books/price/max?value=500
    @GetMapping("/price/max")
    public List<Book> getBooksCheaperThan(@RequestParam("value") Double maxPrice) {
        return bookService.getBooksCheaperThan(maxPrice);
    }

    // GET /api/books/price/range?min=400&max=600
    @GetMapping("/price/range")
    public List<Book> getByPriceRange(@RequestParam Double min, @RequestParam Double max) {
        return bookService.getByPriceRange(min, max);
    }
}
