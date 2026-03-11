package com.hamzaeren.course.spring.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Schema(description = "Kitap Ödünç Alma İsteği")
public class BorrowRequest {

    @NotNull(message = "Kitap ID zorunludur.")
    @Schema(description = "Ödünç alınmak istenen kitabın benzersiz ID'si", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long bookId;
}
