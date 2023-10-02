package com.service.BOOKJEOK.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

public class MemberRequestDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class MemberApplyReqDto {
        @NotNull
        private Long userId;
        @NotNull
        private Long clubId;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class MemberApproveReqDto {
        @NotNull
        private Long memberId;
    }
}
