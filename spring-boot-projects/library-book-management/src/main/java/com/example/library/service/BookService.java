package com.example.library.service;

import com.example.library.exception.BookNotFoundException;
import com.example.library.exception.DuplicateBookException;
import com.example.library.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// All the business logic. Data lives in an ArrayList (no database yet).
@Service
public class BookService {

    private final List<Book> books = new ArrayList<>();
    private int nextId = 1;

    // A few sample books so you can test GET requests immediately
    public BookService() {
        books.add(new Book(nextId++, "Clean Code", "Robert Martin", 550.0, true));
        books.add(new Book(nextId++, "Effective Java", "Joshua Bloch", 700.0, true));
        books.add(new Book(nextId++, "Head First Java", "Kathy Sierra", 450.0, false));
    }

    // ---------- CRUD ----------

    public Book addBook(Book book) {
        checkDuplicate(book.getBookName(), book.getAuthor(), null);
        book.setBookId(nextId++);
        book.setBookName(book.getBookName().trim());
        book.setAuthor(book.getAuthor().trim());
        books.add(book);
        return book;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    public Book getBookById(Integer id) {
        return books.stream()
                .filter(book -> book.getBookId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
    }

    public Book updateBook(Integer id, Book updated) {
        Book existing = getBookById(id);
        checkDuplicate(updated.getBookName(), updated.getAuthor(), id);
        existing.setBookName(updated.getBookName().trim());
        existing.setAuthor(updated.getAuthor().trim());
        existing.setPrice(updated.getPrice());
        existing.setAvailable(updated.getAvailable());
        return existing;
    }

    // Partial update: only the availability changes
    public Book updateAvailability(Integer id, Boolean available) {
        if (available == null) {
            throw new IllegalArgumentException("Availability (true/false) must be provided");
        }
        Book existing = getBookById(id);
        existing.setAvailable(available);
        return existing;
    }

    public void deleteBook(Integer id) {
        boolean removed = books.removeIf(book -> book.getBookId().equals(id));
        if (!removed) {
            throw new BookNotFoundException("Book not found with ID: " + id);
        }
    }

    // ---------- Custom queries ----------

    // 1. Partial match: name contains the keyword (ignore case)
    public List<Book> searchByName(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("Search keyword must not be blank");
        }
        String text = keyword.trim().toLowerCase();
        return books.stream()
                .filter(book -> book.getBookName().toLowerCase().contains(text))
                .toList();
    }

    // 2. Full match: author equals (ignore case)
    public List<Book> getByAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author name must not be blank");
        }
        return books.stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author.trim()))
                .toList();
    }

    // 3. Only books that can be borrowed now
    public List<Book> getAvailableBooks() {
        return books.stream()
                .filter(book -> Boolean.TRUE.equals(book.getAvailable()))
                .toList();
    }

    // 4. Numeric filter: price less than or equal to max
    public List<Book> getBooksCheaperThan(Double maxPrice) {
        if (maxPrice == null || maxPrice <= 0) {
            throw new IllegalArgumentException("Maximum price must be greater than zero");
        }
        return books.stream()
                .filter(book -> book.getPrice() <= maxPrice)
                .toList();
    }

    // 5. Numeric range: min <= price <= max
    public List<Book> getByPriceRange(Double min, Double max) {
        if (min == null || max == null || min < 0 || max < 0) {
            throw new IllegalArgumentException("Minimum and maximum price must be provided and not negative");
        }
        if (min > max) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }
        return books.stream()
                .filter(book -> book.getPrice() >= min && book.getPrice() <= max)
                .toList();
    }

    // ---------- helper ----------

    // ignoreId = the book being updated (so it does not clash with itself)
    private void checkDuplicate(String name, String author, Integer ignoreId) {
        boolean exists = books.stream()
                .filter(book -> ignoreId == null || !book.getBookId().equals(ignoreId))
                .anyMatch(book -> book.getBookName().equalsIgnoreCase(name.trim())
                        && book.getAuthor().equalsIgnoreCase(author.trim()));
        if (exists) {
            throw new DuplicateBookException(
                    "Book '" + name.trim() + "' by " + author.trim() + " already exists");
        }
    }
}
