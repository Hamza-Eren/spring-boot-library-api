package com.hamzaeren.course.spring.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Kitap Bilgisi Yanıtı")
public class BookResponse {
    @Schema(description = "Kitabın benzersiz ID'si", example = "1")
    private Long id;

    @Schema(description = "Kitabın başlığı", example = "1984")
    private String title;

    @Schema(description = "ISBN Numarası", example = "9780451524935")
    private String isbn;

    @Schema(description = "Yayımlanma tarihi", example = "1949-06-08")
    private LocalDate publishedDate;

    @Schema(description = "Kitabın yazar bilgisi")
    private AuthorResponse author;

    @Schema(description = "Kitap şu an müsait mi? (Ödünçte değilse true)", example = "true")
    private boolean isAvailable; // true → şu an ödünçte değil, alınabilir
}
