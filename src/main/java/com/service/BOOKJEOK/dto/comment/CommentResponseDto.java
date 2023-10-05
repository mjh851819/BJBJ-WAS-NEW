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
}
