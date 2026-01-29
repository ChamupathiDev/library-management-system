package com.lib.api.service;

import com.lib.api.model.Book;
import com.lib.api.model.Category;
import com.lib.api.repository.BookRepository;
import com.lib.api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Integer id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> searchByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    public Book updateBook(Integer id, Book bookDetails) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        // Update the fields
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setIsbn(bookDetails.getIsbn());
        book.setGenre(bookDetails.getGenre());
        book.setLanguage(bookDetails.getLanguage());
        // 3. Handle Category - Fetch the full category from DB first
        if (bookDetails.getCategory() != null && bookDetails.getCategory().getId() != null) {
            Category fullCategory = categoryRepository.findById(bookDetails.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            book.setCategory(fullCategory);
        }

        // 3. Handle the 'status' carefully
        // If the request doesn't have a status, we keep the existing database value
        if (bookDetails.getStatus() != null) {
            book.setStatus(bookDetails.getStatus());
        }

        return bookRepository.save(book);
    }

    public void deleteBook(Integer id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        // 1. Delete the physical file first
        if (book.getImageUrl() != null) {
            try {
                Path filePath = Paths.get("library-api/uploads").resolve(book.getImageUrl());
                Files.deleteIfExists(filePath);
                log.info("Deleted book cover file: {}", book.getImageUrl());
            } catch (IOException e) {
                log.error("Could not delete file: {}", e.getMessage());
            }
        }
        bookRepository.delete(book);
    }

    public Book updateBookImage(Integer id, String fileName) {
        Book book = getBookById(id);
        // This creates the URL that matches the database VARCHAR(512) requirement
        book.setImageUrl("/api/books/images/" + fileName);
        return bookRepository.save(book);
    }
}
