package com.service.BOOKJEOK.dto.likedclub;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class LikedClubResponseDto {


    @Getter
    @AllArgsConstructor
    static public class LikedClubSearchPageResDto {
        private int totalCount;
        private List<LikedClubSearchResDto> clubList;
    }

    @Getter
    static public class LikedClubSearchResDto {
        private Long clubId;
        private String title;
        private String contents;
        private String imgUrl;
        private int likes;

        @QueryProjection
        public LikedClubSearchResDto(Long id, String title, String contents, String imgUrl, int likes) {
            this.clubId = id;
            this.title = title;
            this.contents = contents;
            this.imgUrl = imgUrl;
            this.likes = likes;
        }
    }

    @Getter
    @AllArgsConstructor
    static public class LikedClubIdListResDto{
        private int totalCount;
        private List<LikedClubIdResDto> clubList;
    }

    @Getter
    static public class LikedClubIdResDto {
        private Long clubId;

        @QueryProjection
        public LikedClubIdResDto(Long id) {
            this.clubId = id;
        }
    }
}
