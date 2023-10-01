package com.service.BOOKJEOK.repository.club;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.club.Tag;
import com.service.BOOKJEOK.domain.club.TagEntity;
import com.service.BOOKJEOK.dto.club.ClubRequestDto;
import com.service.BOOKJEOK.dto.club.ClubResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.service.BOOKJEOK.dto.club.ClubRequestDto.*;
import static com.service.BOOKJEOK.dto.club.ClubResponseDto.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClubRepositoryTest {

    @Autowired
    private ClubRepository clubRepository;

    @BeforeEach
    public void setUp() {
        System.out.println("초기화 시작");
        /*
        Club club1 = Club.builder()
                .title("abc")
                .tags("소모임,오프라인,온라인")
                .build();
        Club club2 = Club.builder()
                .title("def")
                .tags("온라인")
                .build();
        Club club3 = Club.builder()
                .title("gea")
                .tags("오프라인,온라인")
                .build();
        Club club4 = Club.builder()
                .title("qqq")
                .tags("소모임")
                .build();
        clubRepository.save(club1);
        clubRepository.save(club2);
        clubRepository.save(club3);
        clubRepository.save(club4);*/

        for (int i = 0; i < 10; i++) {
            clubRepository.save(Club.builder()
                    .title("q1" + i)
                    .tags("소모임")
                    .build());
        }
    }

    @Test
    public void searchClub_test() throws Exception {
        //given
        ClubSearchReqDto clubSearchReqDto = ClubSearchReqDto.builder()
                //.keyword("abc")
                //.tags("ONLINE,OFFLINE")
                .sortBy("createdAt")
                .build();

        PageRequest pageRequest = PageRequest.of(0, 2);

        //when
        Page<Club> clubs = clubRepository.searchClub(clubSearchReqDto, pageRequest);


        //then
        Assertions.assertThat(clubs.getTotalPages()).isEqualTo(5);
        Assertions.assertThat(clubs.getTotalElements()).isEqualTo(10);

    }

}