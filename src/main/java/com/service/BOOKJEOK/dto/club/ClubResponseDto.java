package com.service.BOOKJEOK.dto.club;

import com.querydsl.core.annotations.QueryProjection;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.club.TagEntity;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ClubResponseDto {

    @Getter
    public static class ClubCreateResDto {
        private Long id;
        private String title;

        public ClubCreateResDto(Club club) {
            this.id = club.getId();
            this.title = club.getTitle();
        }
    }

    @Getter
    public static class ClubSearchPageResDto {
        private int totalCount;
        private List<ClubSearchResDto> clubList;

        public ClubSearchPageResDto(int totalCount, List<ClubSearchResDto> clubList) {
            this.totalCount = totalCount;
            this.clubList = clubList;
        }
    }


    @Data
    @Getter
    public static class ClubSearchResDto {
        private Long id;
        private String title;
        private String contents;
        private String img_url;
        private List<String> tags;
        private int likes;

        @Builder
        public ClubSearchResDto(Long id, String title, String contents, String img_url, List<TagEntity> tags, int likes) {
            this.id = id;
            this.title = title;
            this.contents = contents;
            this.img_url = img_url;
            this.likes = likes;

            List<String> taglist = tags.stream().map(m ->
                    m.getTag().getValue()
            ).collect(Collectors.toList());
            this.tags = taglist;

        }
    }
}
