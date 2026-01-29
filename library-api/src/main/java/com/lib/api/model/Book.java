package com.lib.api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    private String genre;
    
    private String language;

    @Column(unique = true)
    private String isbn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status = BookStatus.AVAILABLE;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // --- ADD THIS METHOD HERE ---
    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = BookStatus.AVAILABLE;
        }
    }
}