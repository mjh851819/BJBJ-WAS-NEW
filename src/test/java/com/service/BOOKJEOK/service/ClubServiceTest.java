package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.dto.club.ClubRequestDto;
import com.service.BOOKJEOK.dto.club.ClubResponseDto;
import com.service.BOOKJEOK.handler.ex.CustomApiException;
import com.service.BOOKJEOK.repository.ClubRepository;
import com.service.BOOKJEOK.repository.UserRepository;
import com.service.BOOKJEOK.security.ex.JwtException;
import com.service.BOOKJEOK.util.dummy.DummyObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}