package com.hamzaeren.course.spring.repository;

import com.hamzaeren.course.spring.entity.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void testExistsById() {
        // Yeni yazar oluştur
        Author author = new Author("Hamza Eren", "Biyografi Test");
        authorRepository.save(author);

        // id ile veritabanında var mı kontrol et
        boolean exists = authorRepository.existsById(author.getId());

        // true dönmesini bekle
        assertTrue(exists, "Yazar veritabanına kaydedilmiş olmalı");
    }
}