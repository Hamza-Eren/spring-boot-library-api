package com.hamzaeren.course.spring.service;

import com.hamzaeren.course.spring.dto.ProfileResponse;
import com.hamzaeren.course.spring.dto.ProfileUpdate;
import com.hamzaeren.course.spring.entity.Profile;
import com.hamzaeren.course.spring.repository.ProfileRepository;
import com.hamzaeren.course.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Kullanıcı profillerini ve iletişim bilgilerini yöneten servis sınıfı.
 *
 * @author Hamza Eren Sarpdağ
 * @version 1.0
 * @since 2026-03-09
 */
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProfileResponse getProfileById(Long userId) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Profil bulunamadı: ID " + userId));
        return mapToResponse(profile);
    }

    /**
     * Mevcut bir kullanıcının profil (adres, telefon vb.) bilgilerini günceller.
     * Ayrıca e-posta veya kullanıcı adı değişimlerinde çakışmaları (Duplicate)
     * kontrol eder.
     *
     * @param userId  Profili güncellenecek olan kullanıcının ID'si
     * @param request Güncellenmek istenen alanların yeni değerlerini içeren DTO
     * @return Kullanıcının güncellenmiş profil verisi
     * @throws RuntimeException İstenen kullanıcı adı veya e-posta zaten kullanımda
     *                          ise
     */
    @Transactional
    public ProfileResponse updateProfile(Long userId, ProfileUpdate request) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Profil bulunamadı: ID " + userId));

        // orElseThrow() : Doluysa ver, boşsa hata fırlat.
        // ifPresent() : Doluysa işlem yap, boşsa geç.

        if (request.getUsername() != null) {
            userRepository.findByUsername(request.getUsername()).ifPresent(existingUser -> { // Kullanıcı varsa,
                if (!existingUser.getId().equals(userId)) { // Ben değilsem,
                    throw new RuntimeException("Bu kullanıcı adı zaten kullanımda."); // Hata ver.
                }
            });
            profile.getUser().setUsername(request.getUsername());
        }

        if (request.getEmail() != null) {
            userRepository.findByEmail(request.getEmail()).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(userId)) {
                    throw new RuntimeException("Bu e-posta adresi zaten kullanımda.");
                }
            });
            profile.getUser().setEmail(request.getEmail());
        }

        if (request.getFullName() != null) {
            profile.setFullName(request.getFullName());
        }

        // NOT: Buraya öğrenip regex ekleyeceğim.
        if (request.getPhone() != null) {
            profile.setPhone(request.getPhone());
        }

        if (request.getAddress() != null) {
            profile.setAddress(request.getAddress());
        }

        return mapToResponse(profileRepository.save(profile));
    }

    private ProfileResponse mapToResponse(Profile profile) {
        return new ProfileResponse(
                profile.getId(),
                profile.getUser().getUsername(),
                profile.getUser().getEmail(),
                profile.getFullName(),
                profile.getPhone(),
                profile.getAddress());
    }
}
