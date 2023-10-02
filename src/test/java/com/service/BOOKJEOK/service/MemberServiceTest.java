package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.member.Member;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.dto.member.MemberRequestDto;
import com.service.BOOKJEOK.repository.MemberRepository;
import com.service.BOOKJEOK.repository.UserRepository;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import com.service.BOOKJEOK.util.dummy.DummyObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.service.BOOKJEOK.dto.member.MemberRequestDto.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest extends DummyObject {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ClubRepository clubRepository;

    @Test
    public void apply_Test() throws Exception {
        //given
        MemberApplyReqDto req = new MemberApplyReqDto(1L, 1L);

        //stub1
        User myUser = newMockUser(1L, "mjh", "abc@abc.com");
        when(userRepository.findById(any())).thenReturn(Optional.of(myUser));
        //stub2
        User user = newMockUser(2L, "hmj", "cdf@cdf.com");
        Club club = newMockClub(1L, "myclub", user);
        when(clubRepository.findById(any())).thenReturn(Optional.of(club));
        //stub3
        when(memberRepository.findByUserAndClub(any(), any())).thenReturn(Optional.empty());

        //when
        //then
        memberService.apply(req);
    }

    @Test
    public void delete_Test() throws Exception {
        //given
        Long userId = 1L;
        Long clubId = 2L;
        Long myId = userId;

        User myUser = newMockUser(userId, "mjh", "abc@abc.com");
        when(userRepository.findById(any())).thenReturn(Optional.of(myUser));
        //stub2
        User user = newMockUser(clubId, "hmj", "cdf@cdf.com");
        Club club = newMockClub(1L, "myclub", user);
        when(clubRepository.findById(any())).thenReturn(Optional.of(club));
        //stub3
        Member member = Member.builder()
                        .user(myUser)
                        .club(club)
                        .build();
        when(memberRepository.findByUserAndClub(any(), any())).thenReturn(Optional.of(member));

        //when
        //then
        memberService.delete(userId, clubId, myId);
    }

}