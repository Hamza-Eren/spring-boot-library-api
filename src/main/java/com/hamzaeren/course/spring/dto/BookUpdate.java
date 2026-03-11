package com.hamzaeren.course.spring.dto;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Schema(description = "Kitap Güncelleme İsteği (Sadece değiştirilmek istenen alanlar)")
public class BookUpdate {
    @Size(max = 200)
    @Schema(description = "Yeni başlık", example = "1984 (Ciltli)")
    private String title;

    @Size(min = 13, max = 13)
    @Schema(description = "Yeni ISBN", example = "9780451524935")
    private String isbn;

    @Schema(description = "Yeni yayımlanma tarihi", example = "1950-01-01")
    private LocalDate publishedDate;

    @Schema(description = "Yeni yazar ID'si", example = "2")
    private Long authorId;
}
