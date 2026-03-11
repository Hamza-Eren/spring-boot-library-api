package com.hamzaeren.course.spring.repository;

import com.hamzaeren.course.spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// JpaRepository arayüzünde save(entity), findById(id), findAll(), deleteById(id), existsById(id) gibi metotlar mevcut.
// "Şu isimle ara", "Şu tarhiten sonrası" gibi sorgu bazlı metotlar buraya yazılıyor.
// JpaRepository<T, ID> sınıfında T entity tipini, ID ise primary key tipini temsil ediyor.
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}