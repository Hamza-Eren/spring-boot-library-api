package com.hamzaeren.course.spring.config;

import com.hamzaeren.course.spring.entity.Role;
import com.hamzaeren.course.spring.entity.User;
import com.hamzaeren.course.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${seed.admin.password}")
    private String adminPassword;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedUsers();
    }

    private void seedUsers() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User(
                    "admin",
                    "admin@library.com",
                    passwordEncoder.encode(adminPassword),
                    Role.ADMIN);

            // UserService'de olduğu gibi Profile tablosuna da kayıt ekliyoruz.
            // Aksi takdirde /profiles/me istekleri hata fırlatır.
            com.hamzaeren.course.spring.entity.Profile adminProfile = new com.hamzaeren.course.spring.entity.Profile(
                    "Sistem Yöneticisi",
                    null,
                    null);

            admin.setProfile(adminProfile);
            userRepository.save(admin);
            System.out.println("✅ Seed: Admin kullanıcısı (ve profili) oluşturuldu.");
        }
    }
}
