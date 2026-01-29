package com.lib.api.repository;

import com.lib.api.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    // This allows us to search for books by author name
    List<Book> findByAuthorContainingIgnoreCase(String author);
}
