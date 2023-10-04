package com.service.BOOKJEOK.dto.feed;

import com.querydsl.core.annotations.QueryProjection;
import com.service.BOOKJEOK.domain.Comment;
import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.user.User;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class FeedResponseDto {

    @Data
    static public class FeedSearchDetailResDto {
        private Long feedId;
        private Long clubId;
        private UserDto user;
        private String title;
        private String contents;
        private String imgUrl;
        private int likes;
        private String created_at;
        private String updated_at;
        private List<CommentListDto> comments;

        @QueryProjection
        public FeedSearchDetailResDto(Feed feed) {
            this.feedId = feed.getId();
            this.clubId = feed.getClub().getId();
            this.user = new UserDto(feed.getUser());
            this.title = feed.getTitle();
            this.contents = feed.getContents();
            this.imgUrl = feed.getImg_url();
            this.likes = feed.getLikes();
            this.created_at = feed.getCreatedAt().toString();
            this.updated_at = feed.getUpdatedAt().toString();

            List<CommentListDto> collect = feed.getCommentList().stream().map(m ->
                    new CommentListDto(m)).collect(Collectors.toList());
            this.comments = collect;
        }
    }
    @Data
    static private class UserDto {
        private Long userId;
        private String name;
        private String imgUrl;

        public UserDto(User user) {
            this.userId = user.getId();
            this.name = user.getName();
            this.imgUrl = user.getImg_url();
        }
    }
    @Data
    static private class CommentListDto {
        private Long commentId;
        private UserDto user;
        private String contents;
        private String created_at;
        private String updated_at;

        public CommentListDto(Comment comment) {
            this.commentId = comment.getId();
            this.user = new UserDto(comment.getUser());
            this.contents = comment.getContents();
            this.created_at = comment.getCreatedAt().toString();
            this.updated_at = comment.getUpdatedAt().toString();
        }
    }

    @Getter
    static public class FeedSearchPageResDto {
        private int totalCount;
        private List<FeedSearchResDto> feedList;

        public FeedSearchPageResDto(int totalCount, List<FeedSearchResDto> feedList) {
            this.totalCount = totalCount;
            this.feedList = feedList;
        }
    }

    @Getter
    static public class FeedSearchResDto {
        private Long id;
        private String contents;
        private int likes;
        private int commentCount;

        @QueryProjection
        public FeedSearchResDto(Long id, String contents, int likes, int commentCount) {
            this.id = id;
            this.contents = contents;
            this.likes = likes;
            this.commentCount = commentCount;
        }
    }

}
