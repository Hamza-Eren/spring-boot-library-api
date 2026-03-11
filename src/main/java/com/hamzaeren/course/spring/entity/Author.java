package com.hamzaeren.course.spring.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authors", uniqueConstraints = { @UniqueConstraint(columnNames = "name") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE) // Setter üretimini engellemek için
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String bio;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private Set<Book> books = new HashSet<>();

    public Author(String name, String bio) {
        this.name = name;
        this.bio = bio;
    }

    public Author(String name, String bio, User user) {
        this.name = name;
        this.bio = bio;
        this.user = user;
    }

    // Convenience method
    public void addBook(Book book) {
        books.add(book);
        book.setAuthor(this);
    }

    public void removeBook(Book book) {
        books.remove(book);
        book.setAuthor(null);
    }
}