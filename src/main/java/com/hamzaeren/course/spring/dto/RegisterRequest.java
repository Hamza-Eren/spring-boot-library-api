package com.hamzaeren.course.spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Schema(description = "Kullanıcı Kayıt Olma İsteği")
public class RegisterRequest {

    // @Size notasyonu null iken hata vermeyeceğinden @NotBlank
    // veya @NotNull kullanmak gerekir.
    @NotBlank(message = "Kullanıcı adı zorunludur")
    @Size(min = 3, max = 20, message = "Kullanıcı adı 3 ile 20 karakter arasında olmalıdır")
    @Schema(description = "Kayıt olunacak kullanıcı adı (Benzersiz olmalı)", example = "johndoe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank(message = "E-posta zorunludur")
    @Email(message = "E-posta geçerli olmalıdır")
    @Schema(description = "Kayıt olunacak e-posta adresi (Benzersiz olmalı)", example = "john@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "Şifre zorunludur")
    @Schema(description = "Kullanıcının sisteme giriş şifresi", example = "GüçlüŞifre123!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotBlank(message = "Ad soyad zorunludur")
    @Schema(description = "Kullanıcının ad soyad bilgisi", example = "John Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fullName;

    // Optional fields
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Telefon numarası geçerli bir formatta olmalıdır (Örn: +905551234567)")
    @Schema(description = "Kullanıcının opsiyonel telefon bilgisi", example = "+905551234567")
    private String phone;

    @Schema(description = "Kullanıcının opsiyonel açık adresi", example = "Kavaklıdere Mah. No:1 Ankara")
    private String address;
}

// @NotBlank: String alanların null olmamasını ve boşluk (whitespace) dışında en
// az bir karakter içermesini sağlar.
// @NotEmpty: String, Collection, Map veya Array alanların null olmamasını ve
// boyutunun 0'dan büyük olmasını sağlar.
// @NotNull: Bir alanın null olmamasını sağlar (her veri tipi için geçerlidir).
// @Size: String, Collection, Map veya Array alanların boyutunun belirtilen min
// ve max değerleri arasında olmasını sağlar.
// @Email: String alanın geçerli bir e-posta formatında olmasını sağlar.
// @Pattern: String alanın belirtilen düzenli ifadeye (regex) uygun olmasını
// sağlar.
// @Min: Sayısal bir değerin belirtilen alt sınırdan küçük olmamasını sağlar.
// @Max: Sayısal bir değerin belirtilen üst sınırdan büyük olmamasını sağlar.
// @Positive / @PositiveOrZero: Sayısal bir değerin pozitif veya sıfıra
// eşit/büyük olmasını sağlar.
// @Negative / @NegativeOrZero: Sayısal bir değerin negatif veya sıfıra
// eşit/küçük olmasını sağlar.
// @Past / @PastOrPresent: Tarih alanının geçmişte veya bugün/geçmişte olmasını
// sağlar.
// @Future / @FutureOrPresent: Tarih alanının gelecekte veya bugün/gelecekte
// olmasını sağlar.
// @Digits: Sayısal bir alanın tam sayı ve ondalık basamak sayısını sınırlar.
// @AssertTrue / @AssertFalse: Boolean bir alanın değerinin true veya false
// olmasını zorunlu kılar.
// @Valid: Nesne içindeki nesnelerin (nested objects) de validasyon kurallarına
// tabi tutulmasını sağlar.