package com.hamzaeren.course.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Yazar Bilgisi Yanıtı")
public class AuthorResponse {

    @Schema(description = "Yazarın benzersiz kimliği", example = "1")
    private Long id;

    @Schema(description = "Yazarın tam adı", example = "George Orwell")
    private String name;

    @Schema(description = "Yazarın biyografisi", example = "İngiliz romancı ve gazeteci.")
    private String bio;
}
