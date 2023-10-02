package com.service.BOOKJEOK.dto.likedclub;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class LikedClubRequestDto {

    @Getter
    @AllArgsConstructor
    static public class LikedClubCreateReqDto {
        private Long userId;
        private Long clubId;
    }
}
