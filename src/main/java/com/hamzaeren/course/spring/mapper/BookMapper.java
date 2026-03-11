package com.hamzaeren.course.spring.mapper;

import com.hamzaeren.course.spring.dto.BookResponse;
import com.hamzaeren.course.spring.entity.Book;
import com.hamzaeren.course.spring.repository.BorrowRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuthorMapper.class})
public abstract class BookMapper {

    @Autowired
    protected BorrowRepository borrowRepository;

    @Mapping(target = "available", source = "book", qualifiedByName = "checkAvailability")
    public abstract BookResponse toResponse(Book book);

    @Named("checkAvailability")
    protected boolean checkAvailability(Book book) {
        return borrowRepository.findByBookIdAndReturnDateIsNull(book.getId()).isEmpty();
    }
}
