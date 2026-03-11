package com.hamzaeren.course.spring.security;

import com.hamzaeren.course.spring.entity.User;
import com.hamzaeren.course.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// RequiredArgsConstructor, private final ile işaretlenen değişkenleri otomatik olarak constructor oluşturur.
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Spring Security'nin standart arayüzü. Login akışında (username ile)
    // kullanılır.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + username));
        return buildUserDetails(user);
    }

    // JWT filtresi artık bu metodu kullanır. Token'dan gelen id ile kullanıcı
    // yüklenir.
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: ID " + id));
        return buildUserDetails(user);
    }

    private UserDetails buildUserDetails(User user) {
        // Spring Security için kullanıcı adı ve şifre bilgilerini
        // tutan bir UserDetails nesnesi oluşturur.
        // NOT: JWT'de subject olarak id kullandığımız için, buradaki "username"
        // alanına id'yi string olarak yazıyoruz. Böylece isTokenValid() karşılaştırması
        // çalışır.
        return org.springframework.security.core.userdetails.User.builder()
                .username(String.valueOf(user.getId()))
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}