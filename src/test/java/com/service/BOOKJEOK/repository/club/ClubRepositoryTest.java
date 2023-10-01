package com.service.BOOKJEOK.repository.club;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.club.Tag;
import com.service.BOOKJEOK.domain.club.TagEntity;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.dto.club.ClubRequestDto;
import com.service.BOOKJEOK.dto.club.ClubResponseDto;
import com.service.BOOKJEOK.repository.UserRepository;
import com.service.BOOKJEOK.util.dummy.DummyObject;
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
class ClubRepositoryTest extends DummyObject {

    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        System.out.println("초기화 시작");

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

    @Test
    public void findByUserId_Test() {
        //given
        Long clubId = 1L;
        Long userId = 1L;
        String userName= "mjh";
        String email = "abc@abc";
        String myTitle = "MyClub";

        User user = newMockUser(userId, userName, email);
        User userPS = userRepository.save(user);
        Club club = newJpaClub(clubId, myTitle, userPS);
        Club clubPS = clubRepository.save(club);

        //when
        Club res = clubRepository.findByUserId(userPS.getId()).get();
        //System.out.println("테스트 : " + res.getTitle());

        //then
        Assertions.assertThat(res.getTitle()).isEqualTo(myTitle);
        Assertions.assertThat(res.getUser().getName()).isEqualTo(userName);

    }

}