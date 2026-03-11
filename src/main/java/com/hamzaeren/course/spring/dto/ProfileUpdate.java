package com.hamzaeren.course.spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Schema(description = "Profil Güncelleme İsteği (Sadece belirtilen alanlar güncellenir)")
public class ProfileUpdate {

    @Size(min = 3, max = 20, message = "Kullanıcı adı 3 ile 20 karakter arasında olmalıdır")
    @Schema(description = "Yeni kullanıcı adı", example = "johndoe")
    private String username;

    @Email(message = "E-posta geçerli olmalıdır")
    @Schema(description = "Yeni e-posta adresi", example = "john@example.com")
    private String email;

    @Size(min = 2, max = 50, message = "Ad soyad 2 ile 50 karakter arasında olmalıdır")
    @Schema(description = "Kullanıcının yeni ad soyad bilgisi", example = "John Doe")
    private String fullName;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Telefon numarası geçerli bir formatta olmalıdır (Örn: +905551234567)")
    @Schema(description = "Yeni telefon numarası", example = "+905551234567")
    private String phone;

    @Size(min = 5, max = 100, message = "Adres 5 ile 100 karakter arasında olmalıdır")
    @Schema(description = "Yeni iletişim adresi", example = "Yeni adres bilgisi...")
    private String address;
}