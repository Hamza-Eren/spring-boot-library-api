package com.hamzaeren.course.spring.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "borrows", indexes = {
        @Index(name = "idx_borrow_user", columnList = "user_id"),
        @Index(name = "idx_borrow_book", columnList = "book_id")
})
@Getter
@NoArgsConstructor
public class Borrow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate borrowDate;

    @Column
    private LocalDate returnDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    public Borrow(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    // assignUser ve assignBook, Book ve User entity'lerinin convenience
    // metotlarıyla uyumlu çalışır.
    public void assignUser(User user) {
        this.user = user;
    }

    public void assignBook(Book book) {
        this.book = book;
    }

    // returnDate setter yerine niyeti açık bir metot daha iyi bir tasarımdır.
    public void markAsReturned(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}