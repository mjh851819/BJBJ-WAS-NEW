package com.service.BOOKJEOK.dto.club;

import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import lombok.Getter;

public class ClubRequestDto {

    @Getter
    public static class ClubCreateReqDto {
        private Long userId;
        private String title;
        private String img_url;
        private String contents;
        private int max_personnel;
        private String description;
        private String tags;
        private String bookTitle;
        private String author;
        private String publisher;

        public Club toEntity(User user){
            return Club.builder()
                    .id(userId)
                    .title(title)
                    .img_url(img_url)
                    .contents(contents)
                    .max_personnel(max_personnel)
                    .description(description)
                    .tags(tags)
                    .bookTitle(bookTitle)
                    .author(author)
                    .publisher(publisher)
                    .user(user)
                    .build();
        }
    }
}
