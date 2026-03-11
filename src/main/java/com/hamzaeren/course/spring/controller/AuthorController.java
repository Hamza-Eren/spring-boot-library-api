package com.hamzaeren.course.spring.controller;

import com.hamzaeren.course.spring.dto.AuthorRequest;
import com.hamzaeren.course.spring.dto.AuthorResponse;
import com.hamzaeren.course.spring.dto.AuthorUpdate;
import com.hamzaeren.course.spring.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
@Tag(name = "Yazarlar", description = "Yazar yönetimi (CRUD) işlemleri. JWT gerektirir.")
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Yeni Yazar Ekle", description = "Sisteme dışarıdan tarihi veya giriş yapmayacak yeni bir yazar kaydeder. Sadece ADMIN yetkisi gerektirir.")
    public ResponseEntity<AuthorResponse> createAuthor(@Valid @RequestBody AuthorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.createAuthor(request));
    }

    @PostMapping("/assign/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Kullanıcıyı Yazar Yap", description = "Sistemdeki normal bir kullanıcıya YAZAR (AUTHOR) rolü atar ve otomatik yazar profili oluşturur.")
    public ResponseEntity<AuthorResponse> assignAuthorRole(@PathVariable Long userId) {
        return ResponseEntity.ok(authorService.assignAuthorRole(userId));
    }

    @GetMapping
    @Operation(summary = "Tüm Yazarları Listele", description = "Sistemdeki kayıtlı tüm yazarları sayfalı olarak getirir.")
    public ResponseEntity<org.springframework.data.domain.Page<AuthorResponse>> getAllAuthors(org.springframework.data.domain.Pageable pageable) {
        return ResponseEntity.ok(authorService.getAllAuthors(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Yazar Getir (ID ile)", description = "Belirtilen ID'ye sahip yazarın detaylarını getirir.")
    public ResponseEntity<AuthorResponse> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR')")
    @Operation(summary = "Yazar Güncelle", description = "Yazarın gönderilen alanlarını günceller. ADMIN dilediği yazarı, AUTHOR ise sadece kendini güncelleyebilir.")
    public ResponseEntity<AuthorResponse> updateAuthor(
            @PathVariable Long id,
            @Valid @RequestBody AuthorUpdate request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long callerId = Long.parseLong(userDetails.getUsername());
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(authorService.updateAuthor(id, request, callerId, isAdmin));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Yazar Sil", description = "Belirtilen yazarı sistemden siler. Sadece ADMIN yetkisi gerektirir.")
    public ResponseEntity<Map<String, String>> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.ok(Map.of("message", "Yazar başarıyla silindi."));
    }
}
