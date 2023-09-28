package com.service.BOOKJEOK.domain.club;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Embeddable;

@Getter
@Embeddable
public class Book {

    String bookTitle;
    String author;
    String publisher;

}
