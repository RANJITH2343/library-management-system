package com.library.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Table(name="borrow_records")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BorrowRecord {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="book_id", nullable=false)
    private Book book;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="member_id", nullable=false)
    private Member member;

    @Column(name="borrow_date", nullable=false)
    private LocalDate borrowDate;

    @Column(name="due_date", nullable=false)
    private LocalDate dueDate;

    @Column(name="return_date")
    private LocalDate returnDate;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Status status = Status.Borrowed;

    @Builder.Default
    @Column(name="fine_amount", precision=8, scale=2)
    private BigDecimal fineAmount = BigDecimal.ZERO;

    @Column(name="created_at", updatable=false)
    private LocalDateTime createdAt;

    public enum Status { Borrowed, Returned, Overdue }

    @PrePersist protected void onCreate() {
        if(borrowDate==null) borrowDate=LocalDate.now();
        if(dueDate==null)    dueDate=borrowDate.plusDays(14);
        createdAt=LocalDateTime.now();
    }
}
