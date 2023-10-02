package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.LikedClub;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.member.ApprovalStatus;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.dto.likedclub.LikedClubResponseDto;
import com.service.BOOKJEOK.dto.member.MemberResponseDto;
import com.service.BOOKJEOK.repository.likedclub.LikedClubRepository;
import com.service.BOOKJEOK.repository.UserRepository;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import com.service.BOOKJEOK.util.dummy.DummyObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.service.BOOKJEOK.dto.likedclub.LikedClubRequestDto.*;
import static com.service.BOOKJEOK.dto.likedclub.LikedClubResponseDto.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikedClubServiceTest extends DummyObject {

    @InjectMocks
    private LikedClubService likedClubService;

    @Mock
    private LikedClubRepository likedClubRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ClubRepository clubRepository;

    @Test
    public void createLike_Test() throws Exception {
        //given
        User me = newUser("mjh", "abc@abc.com");
        Club myClub = newClub("myclub", me);
        LikedClub tar = LikedClub.builder()
                .user(me)
                .club(myClub)
                .build();

        //stub
        when(userRepository.findById(any())).thenReturn(Optional.of(me));
        when(clubRepository.findById(any())).thenReturn(Optional.of(myClub));
        when(likedClubRepository.findByUserAndClub(any(), any())).thenReturn(Optional.empty());

        //when
        likedClubService.createLike(new LikedClubCreateReqDto(1L, 1L));
        //then
        Assertions.assertThat(myClub.getLikes()).isEqualTo(1);


    }

    @Test
    public void deleteLike_Test() throws Exception {
        //given
        User me = newUser("mjh", "abc@abc.com");
        Club myClub = newClub("myclub", me);
        LikedClub tar = LikedClub.builder()
                .user(me)
                .club(myClub)
                .build();
        myClub.createLike();

        //stub
        when(userRepository.findById(any())).thenReturn(Optional.of(me));
        when(clubRepository.findById(any())).thenReturn(Optional.of(myClub));
        when(likedClubRepository.findByUserAndClub(any(), any())).thenReturn(Optional.of(tar));

        //when
        likedClubService.deleteLike(me.getId(), myClub.getId());

        //then
        Assertions.assertThat(myClub.getLikes()).isEqualTo(0);
    }

    @Test
    public void getLikedClubList_Test() throws Exception {
        //given
        Long userID = 1L;
        PageRequest pageRequest = PageRequest.of(0, 4);

        //stub
        List<LikedClubSearchResDto> dto = new ArrayList<>();
        dto.add(new LikedClubSearchResDto(1L, "a", "a", "a", 0));
        Page<LikedClubSearchResDto> clubPage = new PageImpl<>(dto, pageRequest, 1);
        when(likedClubRepository.searchClubList(any(), any())).thenReturn(clubPage);

        //when
        LikedClubSearchPageResDto res = likedClubService.getLikedClubList(userID, pageRequest);

        //then
        Assertions.assertThat(res.getTotalCount()).isEqualTo(1);
        Assertions.assertThat(res.getClubList().get(0).getTitle()).isEqualTo("a");
    }

    @Test
    public void getLikedClubIdList_Test() throws Exception {
        //given
        Long userID = 1L;

        //stub
        List<LikedClubIdResDto> list = new ArrayList<>();
        list.add(new LikedClubIdResDto(1L));
        when(likedClubRepository.searchClubIdList(any())).thenReturn(list);

        //when
        LikedClubIdListResDto res = likedClubService.getLikedClubIdList(userID);

        //then
        Assertions.assertThat(res.getTotalCount()).isEqualTo(1);
        Assertions.assertThat(res.getClubIdList().get(0).getId()).isEqualTo(userID);
    }
}