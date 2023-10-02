package com.service.BOOKJEOK.dto.member;

import com.querydsl.core.annotations.QueryProjection;
import com.service.BOOKJEOK.domain.member.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
        private Long userId;
        private String img_url;
        private String name;
        private String email;
        private String status;

        @QueryProjection
        public MemberSearchResDto(Long memberId, Long clubId, Long userId, String img_url, String name, String email, ApprovalStatus status) {
            this.memberId = memberId;
            this.clubId = clubId;
            this.userId = userId;
            this.img_url = img_url;
            this.name = name;
            this.email = email;
            this.status = status.getValue();
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
        private Long id;
        private String title;
        private String imgUrl;
        private String contents;
        private int likes;

        @QueryProjection
        public MemberJoiningClubResDto(Long id, String title, String imgUrl, String contents, int likes) {
            this.id = id;
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
        private List<MemberJoiningClubsIdResDto> clubIdList;
    }

    @Getter
    static public class MemberJoiningClubsIdResDto {
        private Long id;

        @QueryProjection
        public MemberJoiningClubsIdResDto(Long id) {
            this.id = id;
        }
    }
}
