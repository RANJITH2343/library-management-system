package com.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name="books")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Book {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="Title is required")
    @Column(nullable=false, length=200)
    private String title;

    @NotBlank(message="Author is required")
    @Column(nullable=false, length=150)
    private String author;

    @NotBlank(message="ISBN is required")
    @Column(nullable=false, unique=true, length=20)
    private String isbn;

    @NotBlank(message="Category is required")
    @Column(nullable=false, length=100)
    private String category;

    @Column(length=150)
    private String publisher;

    @Column(name="published_year")
    private Integer publishedYear;

    @Builder.Default
    @Column(name="total_copies", nullable=false)
    private int totalCopies = 1;

    @Builder.Default
    @Column(name="available_copies", nullable=false)
    private int availableCopies = 1;

    @Column(columnDefinition="TEXT")
    private String description;

    @Column(name="created_at", updatable=false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @PrePersist  protected void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate   protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}
