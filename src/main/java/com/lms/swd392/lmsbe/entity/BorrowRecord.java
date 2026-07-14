package com.lms.swd392.lmsbe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "borrow_records")
public class BorrowRecord {
    @Id
    @Column(name = "borrow_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private BorrowRequest request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id")
    private User borrower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private User staff;

    @Column(name = "borrow_date")
    private Instant borrowDate;

    @Column(name = "due_date")
    private Instant dueDate;

    @Column(name = "return_date")
    private Instant returnDate;

    @Size(max = 20)
    @Column(name = "status", length = 20)
    private String status;


}