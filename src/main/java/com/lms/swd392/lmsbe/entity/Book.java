package com.lms.swd392.lmsbe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 255)
    @Column(name = "author")
    private String author;

    @Size(max = 255)
    @Column(name = "publisher")
    private String publisher;

    @Size(max = 50)
    @Column(name = "isbn", length = 50)
    private String isbn;

    @Column(name = "published_year")
    private Integer publishedYear;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "available_quantity")
    private Integer availableQuantity;

    @Size(max = 20)
    @Column(name = "status", length = 20)
    private String status;

    @ManyToMany
    @JoinTable(name = "book_categories",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new LinkedHashSet<>();

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @OneToMany(mappedBy = "bookId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookImage> images = new ArrayList<>();

}