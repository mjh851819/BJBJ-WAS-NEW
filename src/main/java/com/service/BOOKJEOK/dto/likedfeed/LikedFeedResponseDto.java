package com.service.BOOKJEOK.dto.likedfeed;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.List;

public class LikedFeedResponseDto {

    @Getter
    static public class LikedFeedIdListResDto{
        private int totalCount;
        private List<LikedFeedIdResDto> feedList;

        public LikedFeedIdListResDto(int totalCount, List<LikedFeedIdResDto> feedIdList) {
            this.totalCount = totalCount;
            this.feedList = feedIdList;
        }
    }

    @Getter
    static public class LikedFeedIdResDto{
        private Long feedId;

        @QueryProjection
        public LikedFeedIdResDto(Long feedId) {
            this.feedId = feedId;
        }
    }

}
