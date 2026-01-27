package com.lib.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "books")
@Getter
@Setter
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
}