package com.hamzaeren.course.spring.controller;

import com.hamzaeren.course.spring.dto.BorrowRequest;
import com.hamzaeren.course.spring.dto.BorrowResponse;
import com.hamzaeren.course.spring.service.BorrowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/borrows")
@RequiredArgsConstructor
@Tag(name = "Ödünç İşlemleri", description = "Kitap ödünç alma, iade etme ve kullanıcının okuma geçmişini takip etme işlemleri")
public class BorrowController {

    private final BorrowService borrowService;

    private Long getCurrentUserId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    // Kitap ödünç al
    @PostMapping
    @Operation(summary = "Kitap Ödünç Al", description = "Giriş yapmış olan kullanıcı, ID'si verilen aktif (başkasında olmayan) bir kitabı ödünç alır.")
    public ResponseEntity<BorrowResponse> borrowBook(@Valid @RequestBody BorrowRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(borrowService.borrowBook(getCurrentUserId(), request));
    }

    // Kitabı iade et
    @PatchMapping("/{id}/return")
    @Operation(summary = "Kitap İade Et", description = "Giriş yapmış kullanıcının daha önce ödünç aldığı aktif bir kitabı (kitabın ID'si ile) sisteme iade etmesini sağlar.")
    public ResponseEntity<BorrowResponse> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(borrowService.returnBook(id, getCurrentUserId()));
    }

    // Tüm ödünç geçmişim (iade edilenler dahil)
    @GetMapping("/me")
    @Operation(summary = "Tüm Ödünç Geçmişim", description = "Giriş yapmış kullanıcının hem önceden okuyup iade ettiği hem de şu an elinde olan tüm kitap geçmişini döner.")
    public ResponseEntity<List<BorrowResponse>> getMyBorrows() {
        return ResponseEntity.ok(borrowService.getMyBorrows(getCurrentUserId()));
    }

    // Sadece şu an ödünçteki kitaplarım
    @GetMapping("/me/active")
    @Operation(summary = "Aktif Ödünç Aldıklarım", description = "Giriş yapmış olan kullanıcının şu anda okumakta olduğu (henüz iade etmediği) kitapları listeler.")
    public ResponseEntity<List<BorrowResponse>> getMyActiveBorrows() {
        return ResponseEntity.ok(borrowService.getMyActiveBorrows(getCurrentUserId()));
    }
}
