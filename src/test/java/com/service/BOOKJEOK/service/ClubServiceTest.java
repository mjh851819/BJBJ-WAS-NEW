package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.club.TagEntity;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.handler.ex.CustomApiException;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import com.service.BOOKJEOK.repository.UserRepository;
import com.service.BOOKJEOK.util.dummy.DummyObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.service.BOOKJEOK.dto.club.ClubRequestDto.*;
import static com.service.BOOKJEOK.dto.club.ClubResponseDto.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClubServiceTest extends DummyObject {

    @InjectMocks
    private ClubService clubService;

    @Mock
    private ClubRepository clubRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    public void create_club_test() throws Exception {
        //given
        ClubCreateReqDto clubCreateReqDto = ClubCreateReqDto.builder()
                .userId(1L)
                .title("mjhClub")
                .tags("소모임,오프라인,온라인")
                .build();
        //when
        User user = newMockUser(1L, "mjh", "abc@abc");
        Club club = clubCreateReqDto.toEntity(user);
        //then

        List<TagEntity> tags = club.getTags();

        for (TagEntity s :
                tags) {
            System.out.println("테스트 :" + s.getTag().getValue());
        }
    }

    @Test
    public void createClub_fail_Test() throws Exception {
        //given
        ClubCreateReqDto clubCreateReqDto = ClubCreateReqDto.builder()
                .userId(1L)
                .title("mjhClub")
                .build();

        //stub 1
        User user = newMockUser(1L, "mjh", "abc@abc");
        user.setClub(new Club());
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        //when
        //then
        assertThrows(CustomApiException.class, () -> clubService.createClub(clubCreateReqDto));
    }

    @Test
    public void createClub_success_Test() throws Exception {
        //given
        ClubCreateReqDto clubCreateReqDto = ClubCreateReqDto.builder()
                .userId(1L)
                .title("mjhClub")
                .build();

        //stub 1
        User user = newMockUser(1L, "mjh", "abc@abc");
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        //stub 2
        Club club = newMockClub(1L, "mjhClub");
        when(clubRepository.save(any())).thenReturn(club);
        //stub 3


        //when
        ClubCreateResDto res = clubService.createClub(clubCreateReqDto);

        //then
        Assertions.assertThat(res.getId()).isEqualTo(1L);
        Assertions.assertThat(res.getTitle()).isEqualTo("mjhClub");
    }

    @Test
    public void searchClub_Test() throws Exception {
        //given
        ClubSearchReqDto req = ClubSearchReqDto.builder()
                .sortBy("CreatedAt")
                .tags("ONLINE")
                .keyword("abc")
                .build();

        PageRequest pageRequest = PageRequest.of(0, 1);

        //stub
        Club myClub = Club.builder()
                .id(1L)
                .title("abc")
                .tags("온라인,소모임,수도권")
                .contents("독서모임")
                .img_url("qwer")
                .build();

        List<Club> clubs = new ArrayList<>();
        clubs.add(myClub);
        PageRequest pageable = PageRequest.of(0, 1);
        Page<Club> clubPage = new PageImpl<>(clubs, pageable, 1);

        when(clubRepository.searchClub(any(), any())).thenReturn(clubPage);

        //when
        ClubSearchPageResDto res = clubService.searchClub(req, pageRequest);
        //System.out.println("테스트 : " + res.getClubList().get(0).getTags());

        //then
        Assertions.assertThat(res.getTotalCount()).isEqualTo(1);
        Assertions.assertThat(res.getClubList().size()).isEqualTo(1);

    }
}