package com.hamzaeren.course.spring.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Schema(description = "Yazar Güncelleme İsteği (Sadece değiştirilmek istenen alanlar gönderilebilir)")
public class AuthorUpdate {

    @Size(min = 2, max = 100, message = "Yazar adı 2 ile 100 karakter arasında olmalıdır")
    @Schema(description = "Yazarın yeni adı", example = "George Orwell Updated")
    private String name;

    @Size(max = 2000, message = "Biyografi en fazla 2000 karakter olabilir")
    @Schema(description = "Yazarın yeni biyografisi", example = "Güncellenmiş yazar biyografisi...")
    private String bio;
}