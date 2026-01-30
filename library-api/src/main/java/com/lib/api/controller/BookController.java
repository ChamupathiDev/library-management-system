package com.lib.api.controller;

import com.lib.api.model.Book;
import com.lib.api.service.BookService;
import com.lib.api.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final FileService fileService;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.addBook(book));
    }

    @GetMapping("/search")
    public List<Book> search(@RequestParam String author) {
        return bookService.searchByAuthor(author);
    }

    // Add these to your BookController
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Integer id, @RequestBody Book bookDetails) {
        return ResponseEntity.ok(bookService.updateBook(id, bookDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/upload-image")
    @PreAuthorize("hasRole('LIBRARIAN')") // Restricts access to Librarian role [cite: 45]
    public ResponseEntity<Book> uploadImage(
            @PathVariable Integer id,
            @RequestParam("file") MultipartFile file) {

        String fileName = fileService.saveFile(file);
        Book updatedBook = bookService.updateBookImage(id, fileName);
        return ResponseEntity.ok(updatedBook);
    }
}
