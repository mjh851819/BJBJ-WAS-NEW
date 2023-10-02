package com.service.BOOKJEOK.repository;

import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.member.ApprovalStatus;
import com.service.BOOKJEOK.domain.member.Member;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.dto.member.MemberResponseDto;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import com.service.BOOKJEOK.repository.member.MemberRepository;
import com.service.BOOKJEOK.util.dummy.DummyObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static com.service.BOOKJEOK.dto.member.MemberResponseDto.*;

@DataJpaTest
class MemberRepositoryTest extends DummyObject {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClubRepository clubRepository;

    @Test
    public void findByUserAndClub_Test() throws Exception {
        //given
        User myUser = newMockUser(1L, "mjh", "mjh8518@naver.com");
        User userPS = userRepository.save(myUser);
        Club myClub = newJpaClub(1L, "abc", userPS);
        Club clubPS = clubRepository.save(myClub);
        Member member = Member.builder().club(clubPS).user(userPS).build();
        Member memberPS = memberRepository.save(member);

        //when
        Member findMember = memberRepository.findByUserAndClub(userPS, clubPS).get();

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(memberPS.getId());
    }

    @Test
    public void searchMember_Test() throws Exception {
        //given
        User me = newMockUser(1L, "mjh", "abc@abc.com");
        User user1 = newMockUser(2L, "qwe", "abc@abc.com");
        User mePS = userRepository.save(me);
        User user1PS = userRepository.save(user1);

        Club club1 = newMockClub(1L, "abc", user1PS);
        Club club2 = newMockClub(2L, "cde", user1PS);
        Club club3 = newMockClub(3L, "fgh", user1PS);
        Club club1PS = clubRepository.save(club1);
        Club club2PS = clubRepository.save(club2);
        Club club3PS = clubRepository.save(club3);

        Member member1 = Member.builder().club(club1PS).user(mePS).build();
        Member member2 = Member.builder().club(club2PS).user(mePS).build();
        Member member3 = Member.builder().club(club3PS).user(user1PS).build();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        //when
        PageRequest pageRequest = PageRequest.of(0, 4);
        Page<MemberSearchResDto> res
                = memberRepository.searchMember(mePS.getId(), ApprovalStatus.WAITING, pageRequest);

        //System.out.println("테스트 : " + res.getTotalElements());

        //then
        Assertions.assertThat(res.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(res.getContent().get(0).getUserId()).isEqualTo(mePS.getId());
    }

    @Test
    public void searchJoiningClubs_Test() throws Exception {
        //given
        User me = newMockUser(1L, "mjh", "abc@abc.com");
        User user1 = newMockUser(2L, "qwe", "abc@abc.com");
        User mePS = userRepository.save(me);
        User user1PS = userRepository.save(user1);

        Club club1 = newMockClub(1L, "abc", user1PS);
        Club club2 = newMockClub(2L, "cde", user1PS);
        Club club3 = newMockClub(3L, "fgh", user1PS);
        Club club1PS = clubRepository.save(club1);
        Club club2PS = clubRepository.save(club2);
        Club club3PS = clubRepository.save(club3);

        Member member1 = Member.builder().club(club1PS).user(mePS).build();
        member1.updateState();
        Member member2 = Member.builder().club(club2PS).user(mePS).build();
        member2.updateState();
        Member member3 = Member.builder().club(club3PS).user(user1PS).build();
        member3.updateState();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        //when
        PageRequest pageRequest = PageRequest.of(0, 4);
        Page<MemberJoiningClubResDto> res = memberRepository.searchJoiningClubs(mePS.getId(), pageRequest);

        //then
        Assertions.assertThat(res.getTotalElements()).isEqualTo(2);
    }

}