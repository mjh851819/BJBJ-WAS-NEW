package com.service.BOOKJEOK.dto.comment;

import com.service.BOOKJEOK.domain.Comment;
import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

public class CommentRequestDto {

    @Getter
    @AllArgsConstructor
    public static class CommentCreateReqDto {
        private Long userId;
        private Long feedId;
        @NotNull
        private String contents;

        public Comment toEntity(User user, Feed feed) {
            return Comment.builder()
                    .feed(feed)
                    .user(user)
                    .contents(this.contents)
                    .build();
        }
    }
}
