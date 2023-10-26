package com.service.BOOKJEOK.dto.member;

import com.querydsl.core.annotations.QueryProjection;
import com.service.BOOKJEOK.domain.member.ApprovalStatus;
import com.service.BOOKJEOK.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class MemberResponseDto {

    @AllArgsConstructor
    @Getter
    public static class MemberSearchPageResDto {
        private int totalCount;
        private List<MemberSearchResDto> memberList;
    }

    @Getter
    public static class MemberSearchResDto {
        private Long memberId;
        private Long clubId;
        private UserDto user;
        private String status;

        @QueryProjection
        public MemberSearchResDto(Long memberId, Long clubId, User user, ApprovalStatus status) {
            this.memberId = memberId;
            this.clubId = clubId;
            this.status = status.getValue();
            this.user = new UserDto(user);
        }
    }

    @Data
    static private class UserDto {
        private Long userId;
        private String userName;
        private String imgUrl;

        public UserDto(User user) {
            this.userId = user.getId();
            this.userName = user.getName();
            this.imgUrl = user.getImg_url();
        }
    }

    @Getter
    @AllArgsConstructor
    static public class MemberJoiningClubsPageResDto {
        private int totalCount;
        private List<MemberJoiningClubResDto> clubList;
    }

    @Getter
    static public class MemberJoiningClubResDto {
        private Long clubId;
        private String title;
        private String imgUrl;
        private String contents;
        private int likes;

        @QueryProjection
        public MemberJoiningClubResDto(Long id, String title, String imgUrl, String contents, int likes) {
            this.clubId = id;
            this.title = title;
            this.imgUrl = imgUrl;
            this.contents = contents;
            this.likes = likes;
        }
    }

    @Getter
    @AllArgsConstructor
    static public class MemberJoiningClubsIdListResDto {
        private int totalCount;
        private List<MemberJoiningClubsIdResDto> memberList;
    }

    @Getter
    static public class MemberJoiningClubsIdResDto {
        private Long userId;
        private Long clubId;
        private String status;

        @QueryProjection
        public MemberJoiningClubsIdResDto(Long userId, Long clubId, ApprovalStatus status) {
            this.userId = userId;
            this.clubId = clubId;
            this.status = status.getValue();
        }
    }
}
