package com.hamzaeren.course.spring.repository;

import com.hamzaeren.course.spring.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String token);

    // Yazar/Kullanıcı silindiğinde veya tüm cihazlardan çıkış yapıldığında
    void deleteByUserId(Long userId);
}
