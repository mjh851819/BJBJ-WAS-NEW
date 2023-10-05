package com.service.BOOKJEOK.dto.likedfeed;

import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.LikedFeed;
import com.service.BOOKJEOK.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class LikedFeedRequestDto {

    @Getter
    @AllArgsConstructor
    static public class LikedFeedCreateReqDto {
        private Long feedId;
        private Long userId;

        public LikedFeed toEntity(User user, Feed feed) {
            return LikedFeed.builder()
                    .user(user)
                    .feed(feed)
                    .build();
        }
    }
}
