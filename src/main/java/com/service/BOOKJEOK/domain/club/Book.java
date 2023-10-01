package com.service.BOOKJEOK.domain.club;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@NoArgsConstructor
@Getter
@Embeddable
public class Book {

    String bookTitle;
    String author;
    String publisher;

    @Builder
    public Book(String bookTitle, String author, String publisher) {
        this.bookTitle = bookTitle;
        this.author = author;
        this.publisher = publisher;
    }
}
