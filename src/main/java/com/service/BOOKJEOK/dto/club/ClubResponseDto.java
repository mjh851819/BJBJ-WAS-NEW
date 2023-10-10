package com.service.BOOKJEOK.dto.club;

import com.querydsl.core.annotations.QueryProjection;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.club.TagEntity;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ClubResponseDto {

    @Getter
    public static class ClubCreateResDto {
        private Long id;
        private String title;

        public ClubCreateResDto(Club club) {
            this.id = club.getId();
            this.title = club.getTitle();
        }
    }

    @Getter
    public static class ClubSearchPageResDto {
        private int totalCount;
        private List<ClubSearchResDto> clubList;

        public ClubSearchPageResDto(int totalCount, List<ClubSearchResDto> clubList) {
            this.totalCount = totalCount;
            this.clubList = clubList;
        }
    }


    @Data
    @Getter
    public static class ClubSearchResDto {
        private Long id;
        private String title;
        private String contents;
        private String img_url;

        public ClubSearchResDto(Club club) {
            this.id = club.getId();
            this.title = club.getTitle();
            this.contents = club.getContents();
            this.img_url = club.getImg_url();
        }

        @QueryProjection
        @Builder
        public ClubSearchResDto(Long id, String title, String contents, String img_url) {
            this.id = id;
            this.title = title;
            this.contents = contents;
            this.img_url = img_url;
        }
    }

    @Getter
    public static class ClubSearchDetailResDto {
        private Long userId;
        private Long clubId;
        private String title;
        private String img_url;
        private String contents;
        private int maxPersonnel;
        private String description;
        private List<String> tags;
        private int likes;
        private String status;
        private String bookTitle;
        private String author;
        private String publisher;

        public ClubSearchDetailResDto(Club club) {
            this.userId = club.getUser().getId();
            this.clubId = club.getId();
            this.title = club.getTitle();
            this.img_url = club.getImg_url();
            this.contents = club.getContents();
            this.maxPersonnel = club.getMax_personnel();
            this.description = club.getDescription();
            this.likes = club.getLikes();
            this.status = club.getStatus().getValue();
            this.bookTitle = club.getBook().getBookTitle();
            this.author = club.getBook().getAuthor();
            this.publisher = club.getBook().getPublisher();

            List<TagEntity> tags = club.getTags();
            List<String> taglist = tags.stream().map(m ->
                    m.getTag().getValue()
            ).collect(Collectors.toList());
            this.tags = taglist;

        }
    }
}
