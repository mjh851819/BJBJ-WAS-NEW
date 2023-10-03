package com.service.BOOKJEOK.dto.feed;

import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

public class FeedRequestDto {

    @Getter
    static public class FeedCreateReqDto {

        @NotNull
        private Long userId;
        @NotNull
        private Long clubId;
        @NotNull
        private String title;
        @NotNull
        private String contents;
        @NotNull
        private String imgUrl;

        @Builder
        public FeedCreateReqDto(Long userId, Long clubId, String title, String contents, String imgUrl) {
            this.userId = userId;
            this.clubId = clubId;
            this.title = title;
            this.contents = contents;
            this.imgUrl = imgUrl;
        }

        public Feed toEntity(User user, Club club){
            return Feed.builder()
                    .user(user)
                    .club(club)
                    .title(title)
                    .contents(contents)
                    .img_url(imgUrl)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static public class FeedUpdateReqDto {
        private Long feedId;
        private Long userId;
        private Long clubId;
        private String title;
        private String contents;
        private String imgUrl;
    }
}
