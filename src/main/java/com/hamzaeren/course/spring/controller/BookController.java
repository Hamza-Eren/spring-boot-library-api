package com.hamzaeren.course.spring.controller;

import com.hamzaeren.course.spring.dto.BookRequest;
import com.hamzaeren.course.spring.dto.BookResponse;
import com.hamzaeren.course.spring.dto.BookUpdate;
import com.hamzaeren.course.spring.service.BookService;
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
@RequestMapping("/books")
@RequiredArgsConstructor
@Tag(name = "Kitaplar", description = "Kitap yönetimi (CRUD) işlemleri. JWT gerektirir.")
public class BookController {

    private final BookService bookService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR')")
    @Operation(summary = "Yeni Kitap Ekle", description = "Sisteme yazar ataması ile yeni bir kitap ekler. ADMIN dilediği yazar adına, AUTHOR ise kendi adına ekler.")
    public ResponseEntity<BookResponse> createBook(
            @Valid @RequestBody BookRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long callerId = Long.parseLong(userDetails.getUsername());
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(request, callerId, isAdmin));
    }

    @GetMapping
    @Operation(summary = "Tüm Kitapları Listele", description = "Sistemdeki kitapları sayfalı olarak getirir. İsteğe bağlı olarak belirli bir yazarın (authorId) kitaplarını filtrelemek için kullanılabilir.")
    public ResponseEntity<org.springframework.data.domain.Page<BookResponse>> getAllBooks(
            @RequestParam(required = false) Long authorId,
            org.springframework.data.domain.Pageable pageable) {
        return ResponseEntity.ok(bookService.getAllBooks(authorId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Kitap Getir (ID ile)", description = "Belirtilen ID'ye sahip kitabın detaylarını getirir.")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR')")
    @Operation(summary = "Kitap Güncelle", description = "Kitabın metinsel alanlarını günceller.")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @RequestBody BookUpdate request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long callerId = Long.parseLong(userDetails.getUsername());
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(bookService.updateBook(id, request, callerId, isAdmin));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR')")
    @Operation(summary = "Kitap Sil", description = "Belirtilen kitabı sistemden siler.")
    public ResponseEntity<Map<String, String>> deleteBook(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long callerId = Long.parseLong(userDetails.getUsername());
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        bookService.deleteBook(id, callerId, isAdmin);
        return ResponseEntity.ok(Map.of("message", "Kitap başarıyla silindi."));
    }
}
