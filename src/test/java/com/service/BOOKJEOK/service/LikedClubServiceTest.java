package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.LikedClub;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.dto.likedclub.LikedClubRequestDto;
import com.service.BOOKJEOK.repository.LikedClubRepository;
import com.service.BOOKJEOK.repository.UserRepository;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import com.service.BOOKJEOK.util.dummy.DummyObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.service.BOOKJEOK.dto.likedclub.LikedClubRequestDto.*;
import static org.junit.jupiter.api.Assertions.*;
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

}