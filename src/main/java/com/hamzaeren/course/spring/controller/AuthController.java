package com.hamzaeren.course.spring.controller;

import com.hamzaeren.course.spring.dto.LoginRequest;
import com.hamzaeren.course.spring.dto.RegisterRequest;
import com.hamzaeren.course.spring.entity.User;
import com.hamzaeren.course.spring.service.UserService;
import com.hamzaeren.course.spring.security.JwtService;
import com.hamzaeren.course.spring.service.RefreshTokenService;
import com.hamzaeren.course.spring.entity.RefreshToken;
import com.hamzaeren.course.spring.dto.TokenRefreshRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Kimlik Doğrulama", description = "Kullanıcı kayıt ve giriş işlemleri")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    @Operation(summary = "Kullanıcı Kaydı", description = "Sisteme yeni bir kullanıcı kaydeder ve otomatik olarak boş bir profil oluşturur.")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Kullanıcı başarıyla kaydedildi."));
    }

    @PostMapping("/login")
    @Operation(summary = "Kullanıcı Girişi", description = "Geçerli kimlik bilgileriyle giriş yapılırsa Access ve Refresh token döner.")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userService.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Geçersiz kullanıcı adı"));

        if (!userService.getPasswordEncoder().matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Geçersiz şifre");
        }

        String accessToken = jwtService.generateToken(user.getId());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken.getToken()));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Token Yenileme", description = "Geçerli bir Refresh Token gönderildiğinde yeni bir Access Token döner.")
    public ResponseEntity<Map<String, String>> refresh(@Valid @RequestBody TokenRefreshRequest request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String newAccessToken = jwtService.generateToken(user.getId());
                    return ResponseEntity.ok(Map.of(
                            "accessToken", newAccessToken,
                            "refreshToken", request.getRefreshToken()));
                })
                .orElseThrow(() -> new RuntimeException("Geçersiz veya bulunamayan refresh token!"));
    }

    @PostMapping("/logout")
    @Operation(summary = "Çıkış Yap", description = "Sisteme giriş yapmış kullanıcının refresh token'ını iptal eder (Veritabanından siler).")
    public ResponseEntity<Map<String, String>> logout(@Valid @RequestBody TokenRefreshRequest request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(token -> {
                    refreshTokenService.deleteByUserId(token.getUser().getId());
                    return ResponseEntity.ok(Map.of("message", "Başarıyla çıkış yapıldı ve token iptal edildi."));
                })
                .orElseThrow(() -> new RuntimeException("Geçersiz refresh token!"));
    }
}