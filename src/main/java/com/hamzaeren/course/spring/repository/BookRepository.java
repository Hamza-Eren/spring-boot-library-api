package com.hamzaeren.course.spring.repository;

import com.hamzaeren.course.spring.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);

    Optional<Book> findByIsbn(String isbn);

    org.springframework.data.domain.Page<Book> findByAuthorId(Long authorId, org.springframework.data.domain.Pageable pageable);
}
