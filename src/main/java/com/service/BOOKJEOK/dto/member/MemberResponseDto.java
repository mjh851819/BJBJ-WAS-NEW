package com.service.BOOKJEOK.dto.member;

import com.querydsl.core.annotations.QueryProjection;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.club.TagEntity;
import com.service.BOOKJEOK.domain.member.ApprovalStatus;
import com.service.BOOKJEOK.dto.club.ClubResponseDto;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class MemberResponseDto {

    @Getter
    public static class MemberSearchPageResDto {
        private int totalCount;
        private List<MemberSearchResDto> memberList;

        public MemberSearchPageResDto(int totalCount, List<MemberSearchResDto> memberList) {
            this.totalCount = totalCount;
            this.memberList = memberList;
        }
    }

    @Data
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
}
