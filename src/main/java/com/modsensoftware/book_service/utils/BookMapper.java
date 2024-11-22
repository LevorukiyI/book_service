package com.modsensoftware.book_service.utils;

import com.modsensoftware.book_service.dtos.BookDTO;
import com.modsensoftware.book_service.dtos.requests.BookRequest;
import com.modsensoftware.book_service.models.BookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookEntity toEntity(BookRequest bookRequest);

    @Mapping(target = "id", source = "bookId")
    BookEntity toEntity(Long bookId, BookRequest bookRequest);

    BookDTO toDTO(BookEntity bookEntity);
}