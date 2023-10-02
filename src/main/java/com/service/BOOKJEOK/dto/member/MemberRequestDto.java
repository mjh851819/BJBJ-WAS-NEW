package com.service.BOOKJEOK.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class MemberRequestDto {

    @AllArgsConstructor
    @Getter
    public static class MemberApplyReqDto {
        private Long userId;
        private Long clubId;
    }
}
