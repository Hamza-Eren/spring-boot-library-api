package com.hamzaeren.course.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Kullanıcı Profil Bilgisi Yanıtı")
public class ProfileResponse {
    @Schema(description = "Profilin bağlı olduğu kullanıcının ID'si", example = "1")
    private Long id;

    @Schema(description = "Kullanıcının sisteme giriş yaptığı adı", example = "admin")
    private String username;

    @Schema(description = "Kullanıcının iletişim e-posta adresi", example = "admin@library.com")
    private String email;

    @Schema(description = "Kullanıcının ad soyad bilgisi", example = "Sistem Yöneticisi")
    private String fullName;

    @Schema(description = "Kullanıcının telefon numarası", example = "+905551234567")
    private String phone;

    @Schema(description = "Kullanıcının açık adres bilgisi", example = "Örnek Mah. Test Sok. No:1 Ankara")
    private String address;
}
