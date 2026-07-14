package com.lms.swd392.lmsbe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "book_images")
public class BookImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "book_id", nullable = false)
    private Integer bookId;

    @Size(max = 500)
    @NotNull
    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @ColumnDefault("false")
    @Column(name = "is_primary")
    private Boolean isPrimary;

    @ColumnDefault("now()")
    @Column(name = "created_at")
    private Instant createdAt;


}