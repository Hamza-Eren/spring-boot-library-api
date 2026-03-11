package com.hamzaeren.course.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ödünç Alma İşlemi Yanıtı")
public class BorrowResponse {
    @Schema(description = "Ödünç işleminin benzersiz ID'si", example = "42")
    private Long id;

    @Schema(description = "Ödünç alınan kitabın ID'si", example = "1")
    private Long bookId;

    @Schema(description = "Ödünç alınan kitabın başlığı", example = "1984")
    private String bookTitle;

    @Schema(description = "Ödünç alınan kitabın ISBN numarası", example = "9780451524935")
    private String bookIsbn;

    @Schema(description = "Kitabın kasadan çıkış (ödünç alınma) tarihi", example = "2026-03-09")
    private LocalDate borrowDate;

    @Schema(description = "Kitabın iade edildiği tarih (Henüz iade edilmediyse null döner)", example = "null | 2026-03-15")
    private LocalDate returnDate; // null ise henüz iade edilmemiş
}
