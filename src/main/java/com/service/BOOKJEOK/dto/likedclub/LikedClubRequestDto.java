package com.service.BOOKJEOK.dto.likedclub;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

public class LikedClubRequestDto {

    @Getter
    @AllArgsConstructor
    static public class LikedClubCreateReqDto {
        @NotNull
        private Long userId;
        @NotNull
        private Long clubId;
    }
}
