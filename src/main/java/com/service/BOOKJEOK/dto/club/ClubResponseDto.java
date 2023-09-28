package com.service.BOOKJEOK.dto.club;

import com.service.BOOKJEOK.domain.club.Club;

public class ClubResponseDto {

    public static class ClubCreateResDto {
        private Long id;
        private String title;

        public ClubCreateResDto(Club club) {
            this.id = club.getId();
            this.title = club.getTitle();
        }
    }
}
