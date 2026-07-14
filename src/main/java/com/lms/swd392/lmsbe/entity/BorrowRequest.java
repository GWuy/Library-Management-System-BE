package com.lms.swd392.lmsbe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "borrow_requests")
public class BorrowRequest {
    @Id
    @Column(name = "request_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id")
    private User borrower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private User staff;

    @Column(name = "request_date")
    private Instant requestDate;

    @Column(name = "response_date")
    private Instant responseDate;

    @Size(max = 20)
    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "note", length = Integer.MAX_VALUE)
    private String note;


}