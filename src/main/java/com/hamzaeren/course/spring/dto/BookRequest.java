package com.hamzaeren.course.spring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Schema(description = "Kitap Ekleme İsteği")
public class BookRequest {
    @NotBlank(message = "Kitap adı zorunludur.")
    @Size(max = 200)
    @Schema(description = "Kitabın başlığı", example = "1984", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @NotBlank(message = "ISBN zorunludur.")
    @Size(min = 13, max = 13)
    @Schema(description = "13 haneli ISBN numarası", example = "9780451524935", requiredMode = Schema.RequiredMode.REQUIRED)
    private String isbn;

    @NotNull(message = "Yayın tarihi zorunludur.")
    @Schema(description = "Kitabın yayımlanma tarihi", example = "1949-06-08", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate publishedDate;

    @Schema(description = "Kitabın yazarının benzersiz ID'si (AUTHOR iseniz boş bırakabilirsiniz, ADMIN iseniz zorunludur)", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long authorId;
}
