package com.hamzaeren.course.spring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Schema(description = "Kullanıcı Giriş İsteği")
public class LoginRequest {

    @NotBlank(message = "Kullanıcı adı zorunludur")
    @Size(min = 3, max = 20, message = "Kullanıcı adı 3 ile 20 karakter arasında olmalıdır")
    @Schema(description = "Giriş yapılacak kullanıcı adı", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank(message = "Şifre zorunludur")
    @Schema(description = "Kullanıcı şifresi", example = "admin123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
