package com.example.bookapi.dto.response;

import com.example.bookapi.entity.Book;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {

    private Long id;
    private String title;
    @JsonProperty("author_name")
    private String authorName;
    private String description;
    private String owner;
    @JsonProperty("book_cover")
    private String bookCover;
    private double rate;
    private boolean archived;
    private boolean shareable;

    public static BookResponse fromBook(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .description(book.getDescription())
                .owner(book.getOwner().fullName())
                .rate(book.getRate())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                .bookCover(book.getBookCover())
                .build();
    }
}
