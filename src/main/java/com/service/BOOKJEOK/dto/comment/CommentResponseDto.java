package com.service.BOOKJEOK.dto.comment;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class CommentResponseDto {

    @AllArgsConstructor
    @Getter
    static public class CommentSearchPageResDto {

        private int totalCount;
        private List<CommentSearchResDto> commentList;
    }

    @Getter
    static public class CommentSearchResDto {
        private Long feedId;
        private String contents;

        @QueryProjection
        public CommentSearchResDto(Long feedId, String contents) {
            this.feedId = feedId;
            this.contents = contents;
        }
    }

    @AllArgsConstructor
    @Getter
    static public class CommentDetailPageResDto {

        private int totalCount;
        private List<CommentDetailResDto> commentList;
    }

    @Getter
    static public class CommentDetailResDto {
        private Long userId;
        private String userName;
        private String imgUrl;

        private Long commentId;
        private String contents;

        @QueryProjection
        public CommentDetailResDto(Long userId, String userName, String imgUrl, Long commentId, String contents) {
            this.userId = userId;
            this.userName = userName;
            this.imgUrl = imgUrl;
            this.commentId = commentId;
            this.contents = contents;
        }
    }
}
