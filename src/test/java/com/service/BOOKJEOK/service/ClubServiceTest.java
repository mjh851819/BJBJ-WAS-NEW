package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.club.TagEntity;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.handler.ex.CustomApiException;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import com.service.BOOKJEOK.repository.user.UserRepository;
import com.service.BOOKJEOK.repository.comment.CommentRepository;
import com.service.BOOKJEOK.repository.feed.FeedRepository;
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
    @Mock
    private FeedRepository feedRepository;
    @Mock
    private CommentRepository commentRepository;

    @Test
    public void create_club_test() throws Exception {
        //given
        ClubCreateReqDto clubCreateReqDto = ClubCreateReqDto.builder()
                .userId(1L)
                .title("mjhClub")
                .tags("소모임,오프라인,온라인")
                .max_personnel(3)
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
                .tags("온라인,오프라인")
                .build();

        //stub 1
        User user = newMockUser(1L, "mjh", "abc@abc");
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        //stub 2
        Club club = newMockClub(1L, "mjhClub", user);
        when(clubRepository.save(any())).thenReturn(club);
        //stub 3


        //when
        ClubCreateResDto res = clubService.createClub(clubCreateReqDto);

        //then
        Assertions.assertThat(res.getClubId()).isEqualTo(1L);
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

        //then
        Assertions.assertThat(res.getTotalCount()).isEqualTo(1);
        Assertions.assertThat(res.getClubList().size()).isEqualTo(1);
    }

    @Test
    public void findClubById_Test() throws Exception {
        //given
        Long clubId = 1L;
        Long userId = 1L;
        String userName= "mjh";
        String email = "abc@abc";
        String myTitle = "MyClub";

        //when
        User user = newMockUser(userId, userName, email);
        Club club = newMockClub(clubId, "MyClub", user);
        when(clubRepository.findById(clubId)).thenReturn(Optional.of(club));

        ClubSearchDetailResDto res = clubService.findClubById(clubId);

        //then
        Assertions.assertThat(res.getTitle()).isEqualTo(myTitle);
        Assertions.assertThat(res.getClubId()).isEqualTo(clubId);
        Assertions.assertThat(res.getUserId()).isEqualTo(userId);
    }

    @Test
    public void findClubByUserId_Test() throws Exception {
        //given
        Long clubId = 1L;
        Long userId = 1L;
        String userName= "mjh";
        String email = "abc@abc";
        String myTitle = "MyClub";

        //when
        User user = newMockUser(userId, userName, email);
        Club club = newMockClub(clubId, "MyClub", user);
        when(clubRepository.findByUserId(userId)).thenReturn(Optional.of(club));

        ClubSearchDetailResDto res = clubService.findClubByUserId(userId);

        //then
        Assertions.assertThat(res.getTitle()).isEqualTo(myTitle);
        Assertions.assertThat(res.getClubId()).isEqualTo(clubId);
        Assertions.assertThat(res.getUserId()).isEqualTo(userId);
    }

    @Test
    public void updateClub() throws Exception {
        //given
        Long clubId = 1L;
        Long userId = 1L;
        String userName= "mjh";
        String email = "abc@abc";
        String myTitle = "MyClub";

        User user = newMockUser(userId, userName, email);
        Club club = newMockClub(clubId, "MyClub", user);
        ClubUpdateReqDto req = ClubUpdateReqDto.builder()
                .clubId(1L)
                .title("update")
                .img_url("update")
                .contents("update")
                .max_personnel(1)
                .description("update")
                .tags("소모임")
                .bookTitle("update")
                .author("update")
                .publisher("update")
                .build();

        //when
        when(clubRepository.findByUserId(any())).thenReturn(Optional.of(club));

        //then
        clubService.updateClub(req, userId);
    }

    @Test
    public void deleteClub_Test() throws Exception {
        //given
        Long clubId = 1L;
        Long userId = 1L;
        String userName= "mjh";
        String email = "abc@abc";
        String myTitle = "MyClub";

        User user = newMockUser(userId, userName, email);
        Club club = newMockClub(clubId, "MyClub", user);

        //when
        when(clubRepository.findByUserId(any())).thenReturn(Optional.of(club));

        //then
        clubService.deleteClub(userId);
    }

    @Test
    public void searchClubForMain_Test() throws Exception {
        //given

        //stub
        User user = newUser("mjh", "abc");
        List<ClubSearchResDto> list = new ArrayList<>();
        list.add(ClubSearchResDto.builder()
                        .id(1L)
                        .title("abc")
                        .contents("contents")
                        .img_url("qwer")
                .build());
        when(clubRepository.find4ClubList(any())).thenReturn(list);

        //when
        ClubSearchPageResDto res = clubService.searchClubForMain("likes");

        //then
        Assertions.assertThat(res.getTotalCount()).isEqualTo(res.getClubList().size());
    }


}