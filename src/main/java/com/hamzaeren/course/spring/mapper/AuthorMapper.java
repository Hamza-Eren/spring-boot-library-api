package com.hamzaeren.course.spring.mapper;

import com.hamzaeren.course.spring.dto.AuthorResponse;
import com.hamzaeren.course.spring.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthorMapper {
    AuthorResponse toResponse(Author author);
}
