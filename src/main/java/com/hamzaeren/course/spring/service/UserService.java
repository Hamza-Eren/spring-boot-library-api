package com.hamzaeren.course.spring.service;

import com.hamzaeren.course.spring.dto.RegisterRequest;
import com.hamzaeren.course.spring.entity.Profile;
import com.hamzaeren.course.spring.entity.User;
import com.hamzaeren.course.spring.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Kullanıcı yönetimi ve kimlik doğrulama süreçlerini yöneten servis sınıfı.
 * Yeni kullanıcı kaydı oluşturulurken şifrelerin şifrelenmesi ve profil
 * bağlantılarının
 * kurulması gibi temel güvenlik işlemlerinden sorumludur.
 *
 * @author Hamza Eren Sarpdağ
 * @version 1.0
 * @since 2026-03-09
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    @Getter
    private final PasswordEncoder passwordEncoder;

    // @Transactional: Bu metodun bir transaction içinde çalışmasını sağlar.
    // Eğer metod içinde bir hata olursa, işlem geri alınır (rollback).
    /**
     * Sisteme yeni bir kullanıcı kaydeder ve eş zamanlı olarak boş bir profil
     * nesnesi oluşturur.
     * Kullanıcı şifresi veritabanına kaydedilmeden önce PasswordEncoder ile
     * şifrelenir (hashlenir).
     *
     * @param dto Kullanıcının kayıt sırasında girdiği bilgileri içeren DTO
     * @throws RuntimeException Kullanıcı adı veya e-posta adresi önceden alınmışsa
     */
    @Transactional
    public void register(RegisterRequest dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Bu kullanıcı adı zaten kullanımda.");
        } else if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Bu e-posta adresi zaten kullanımda.");
        }
        User user = new User(dto.getUsername(), dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()));

        Profile profile = new Profile(
                dto.getFullName(),
                dto.getPhone(),
                dto.getAddress());
        user.setProfile(profile);
        userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}