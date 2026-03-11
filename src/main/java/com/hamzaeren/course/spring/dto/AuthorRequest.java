package com.hamzaeren.course.spring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Schema(description = "Yazar Ekleme İsteği")
public class AuthorRequest {

    @NotBlank(message = "Yazar adı zorunludur")
    @Size(min = 2, max = 100, message = "Yazar adı 2 ile 100 karakter arasında olmalıdır")
    @Schema(description = "Yazarın tam adı", example = "George Orwell", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Size(max = 2000, message = "Biyografi en fazla 2000 karakter olabilir")
    @Schema(description = "Yazarın biyografisi (isteğe bağlı)", example = "İngiliz romancı ve gazeteci. En bilinen eserleri 1984 ve Hayvan Çiftliği'dir.")
    private String bio;
}
