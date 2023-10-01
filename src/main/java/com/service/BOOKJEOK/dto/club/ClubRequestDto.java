package com.service.BOOKJEOK.dto.club;

import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class ClubRequestDto {

    @Builder
    @Getter
    public static class ClubCreateReqDto {
        @NotNull
        private Long userId;
        @NotEmpty
        @Size(min = 2, max = 10)
        private String title;
        @NotEmpty
        private String img_url;
        @NotEmpty
        @Size(min = 1, max = 30)
        private String contents;
        @NotNull
        private Integer max_personnel;
        @Size(min = 1, max = 300)
        private String description;
        @NotEmpty
        private String tags;
        @NotEmpty
        @Size(min = 1, max = 20)
        private String bookTitle;
        @NotEmpty
        @Size(min = 1, max = 15)
        private String author;
        @NotEmpty
        @Size(min = 1, max = 10)
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

    @Data
    @Builder
    @Getter
    public static class ClubSearchReqDto {
        @NotNull
        private String sortBy;
        private String tags;
        @Size(min = 1, max = 10)
        private String keyword;
    }

    @Builder
    @Getter
    public static class ClubUpdateReqDto {
        @NotNull
        private Long clubId;
        @NotEmpty
        @Size(min = 2, max = 10)
        private String title;
        @NotEmpty
        private String img_url;
        @NotEmpty
        @Size(min = 1, max = 30)
        private String contents;
        @NotNull
        private int max_personnel;
        @Size(min = 1, max = 300)
        private String description;
        @NotEmpty
        private String tags;
        @NotEmpty
        @Size(min = 1, max = 20)
        private String bookTitle;
        @NotEmpty
        @Size(min = 1, max = 15)
        private String author;
        @NotEmpty
        @Size(min = 1, max = 10)
        private String publisher;
    }
}
