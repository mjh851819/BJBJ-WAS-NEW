package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.member.ApprovalStatus;
import com.service.BOOKJEOK.domain.member.Member;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.repository.member.MemberRepository;
import com.service.BOOKJEOK.repository.user.UserRepository;
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

import static com.service.BOOKJEOK.dto.member.MemberRequestDto.*;
import static com.service.BOOKJEOK.dto.member.MemberResponseDto.*;
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

    @Test
    public void approve_Test() throws Exception {
        //given
        MemberApproveReqDto req = new MemberApproveReqDto(1L);
        Member member = Member.builder()
                .user(newUser("mjh", "abc@abc.com"))
                .club(newClub("myclub", newUser("abc", "def@def.com")))
                .build();
        //stub
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        //when
        //then
        memberService.approve(req);
    }

    @Test
    public void getMemberList_Test() throws Exception {
        //given
        Long userId = 1L;
        ApprovalStatus status = ApprovalStatus.WAITING;
        PageRequest pageRequest = PageRequest.of(0, 4);

        //stub
        List<MemberSearchResDto> dto = new ArrayList<>();
        dto.add(new MemberSearchResDto(1L, 1L, 1L, "a", "a", "a", ApprovalStatus.CONFIRMED));
        Page<MemberSearchResDto> clubPage = new PageImpl<>(dto, pageRequest, 1);

        when(memberRepository.searchMember(any(), any(), any())).thenReturn(clubPage);

        //when
        MemberSearchPageResDto res = memberService.getMemberList(userId, "WAITING", pageRequest);

        //then
        Assertions.assertThat(res.getMemberList().get(0).getName()).isEqualTo("a");
        Assertions.assertThat(res.getTotalCount()).isEqualTo(1);
    }

    @Test
    public void getJoiningClubList() throws Exception {
        //given
        Long userId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 4);

        //stub
        List<MemberJoiningClubResDto> dto = new ArrayList<>();
        dto.add(new MemberJoiningClubResDto(1L, "myclub", "abc", "hello", 1));
        Page<MemberJoiningClubResDto> clubPage = new PageImpl<>(dto, pageRequest, 1);

        when(memberRepository.searchJoiningClubs(any(), any())).thenReturn(clubPage);

        //when
        MemberJoiningClubsPageResDto res = memberService.getJoiningClubList(userId, pageRequest);

        //then
        Assertions.assertThat(res.getClubList().get(0).getTitle()).isEqualTo("myclub");
        Assertions.assertThat(res.getTotalCount()).isEqualTo(1);
    }

    @Test
    public void getJoiningClubIds() throws Exception {
        //given
        Long userId = 1L;

        //stub
        List<MemberJoiningClubsIdResDto> dtos = new ArrayList<>();
        dtos.add(new MemberJoiningClubsIdResDto(userId));
        when(memberRepository.searchJoiningClubIds(any(), any())).thenReturn(dtos);

        //when
        MemberJoiningClubsIdListResDto res1 = memberService.getJoiningClubIds(userId, ApprovalStatus.CONFIRMED.getValue());
        MemberJoiningClubsIdListResDto res2 = memberService.getJoiningClubIds(userId, ApprovalStatus.WAITING.getValue());

        //then
        Assertions.assertThat(res1.getTotalCount()).isEqualTo(1);
        Assertions.assertThat(res2.getTotalCount()).isEqualTo(1);

    }

}