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
        private Long clubId;
        private String title;

        public ClubCreateResDto(Club club) {
            this.clubId = club.getId();
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
        private Long clubId;
        private String title;
        private String contents;
        private String imgUrl;

        public ClubSearchResDto(Club club) {
            this.clubId = club.getId();
            this.title = club.getTitle();
            this.contents = club.getContents();
            this.imgUrl = club.getImg_url();
        }

        @QueryProjection
        @Builder
        public ClubSearchResDto(Long id, String title, String contents, String img_url) {
            this.clubId = id;
            this.title = title;
            this.contents = contents;
            this.imgUrl = img_url;
        }
    }

    @Getter
    public static class ClubSearchDetailResDto {
        private Long userId;
        private Long clubId;
        private String title;
        private String imgUrl;
        private String contents;
        private int maxPersonnel;
        private String description;
        private String tags = "";
        private int likes;
        private String status;
        private String bookTitle;
        private String author;
        private String publisher;

        public ClubSearchDetailResDto(Club club) {
            this.userId = club.getUser().getId();
            this.clubId = club.getId();
            this.title = club.getTitle();
            this.imgUrl = club.getImg_url();
            this.contents = club.getContents();
            this.maxPersonnel = club.getMax_personnel();
            this.description = club.getDescription();
            this.likes = club.getLikes();
            this.status = club.getStatus().getValue();
            this.bookTitle = club.getBook().getBookTitle();
            this.author = club.getBook().getAuthor();
            this.publisher = club.getBook().getPublisher();

            List<TagEntity> tags = club.getTags();
            for(int i = 0; i < tags.size(); i++){
                this.tags += tags.get(i).getTag().getValue();
                if(i != tags.size() - 1) this.tags += ",";
            }

        }
    }
}
