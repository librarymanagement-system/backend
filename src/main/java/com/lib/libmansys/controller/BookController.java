package com.lib.libmansys.controller;
import com.lib.libmansys.dto.CreateBookRequest;
import com.lib.libmansys.entity.Book;
import com.lib.libmansys.entity.Enum.BookStatus;
import com.lib.libmansys.service.BookService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private static final Logger log = LoggerFactory.getLogger(BookController.class);


    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Endpoint to add a new book
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody CreateBookRequest createBookRequest) {
        Book savedBook = bookService.addBook(createBookRequest);
        return ResponseEntity.ok(savedBook);
    }

    // Endpoint to update an existing book
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(id, book);
        return ResponseEntity.ok(updatedBook);
    }

    // Endpoint to delete a book
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }

    // Endpoint to retrieve all books
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.findAllBooks();
        return ResponseEntity.ok(books);
    }


    @GetMapping("/search")
    public ResponseEntity<List<Book>> getBooksByTitle(@RequestParam String title) {
        List<Book> books = bookService.findBooksByTitle(title);
        return ResponseEntity.ok(books);
    }


    @GetMapping("/filter")
    public ResponseEntity<List<Book>> getBooksByFilters(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String publisher) {
        List<Book> books = bookService.findBooksByFilters(author, genre, publisher);
        return ResponseEntity.ok(books);
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<List<Book>> getBooksByStatus(@PathVariable BookStatus status) {
        List<Book> books = bookService.findBooksByStatus(status);
        return ResponseEntity.ok(books);
    }
    @GetMapping("/export/books")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> exportBooks(HttpServletResponse response) {
        try {
            bookService.exportBooks(response);
            return ResponseEntity.ok("Kitaplar başarıyla dışa aktarıldı.");
        } catch (IOException e) {
            log.error("Dosya dışa aktarma sırasında hata oluştu", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Dosya dışa aktarılamadı.");
        }
    }
}
