package com.hamzaeren.course.spring.controller;

import com.hamzaeren.course.spring.dto.ProfileResponse;
import com.hamzaeren.course.spring.dto.ProfileUpdate;
import com.hamzaeren.course.spring.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
@Tag(name = "Kullanıcı Profili", description = "Kullanıcıların kendi profil adres ve iletişim bilgilerini yönetmesi")
public class ProfileController {

    private final ProfileService profileService;

    // SecurityContextHolder, JwtAuthenticationFilter tarafından set edilir.
    // UserDetails.getUsername() artık kullanıcının id'sini (String) döndürür.
    private Long getCurrentUserId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("/me")
    @Operation(summary = "Profilimi Getir", description = "JWT token'ı kullanarak istek atan asıl kullanıcının güncel profil bilgilerini döndürür.")
    public ResponseEntity<ProfileResponse> getMyProfile() {
        return ResponseEntity.ok(profileService.getProfileById(getCurrentUserId()));
    }

    @PatchMapping("/me")
    @Operation(summary = "Profilimi Güncelle", description = "Kullanıcının profil bilgilerini (isim, telefon, adres) kısmi olarak günceller.")
    public ResponseEntity<ProfileResponse> updateMyProfile(@Valid @RequestBody ProfileUpdate request) {
        return ResponseEntity.ok(profileService.updateProfile(getCurrentUserId(), request));
    }
}
