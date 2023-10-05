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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static com.service.BOOKJEOK.dto.member.MemberResponseDto.*;

@DataJpaTest
class MemberRepositoryTest extends DummyObject {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClubRepository clubRepository;

    private Long userId;
    private Long club1Id;
    private Long club2Id;
    private Long club3Id;
    private Long member1Id;
    private Long member2Id;
    private Long member3Id;

    @BeforeEach
    public void SetUp() {
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
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        userId = mePS.getId();
        club1Id = club1PS.getId();
        club2Id = club2PS.getId();
        club3Id = club3PS.getId();
        member1Id = member1.getId();
        member2Id = member2.getId();
        member3Id = member3.getId();
    }

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

        //when
        PageRequest pageRequest = PageRequest.of(0, 4);
        Page<MemberSearchResDto> res
                = memberRepository.searchMember(userId, ApprovalStatus.WAITING, pageRequest);

        //System.out.println("테스트 : " + res.getTotalElements());

        //then
        Assertions.assertThat(res.getTotalElements()).isEqualTo(0);
    }

    @Test
    public void searchJoiningClubs_Test() throws Exception {
        //given

        //when
        PageRequest pageRequest = PageRequest.of(0, 4);
        Page<MemberJoiningClubResDto> res = memberRepository.searchJoiningClubs(userId, pageRequest);

        //then
        Assertions.assertThat(res.getTotalElements()).isEqualTo(2);
    }

    @Test
    public void searchJoiningClubIds() throws Exception {
        //given

        //when
        List<MemberJoiningClubsIdResDto> res1 = memberRepository.searchJoiningClubIds(userId, ApprovalStatus.WAITING);
        List<MemberJoiningClubsIdResDto> res2 = memberRepository.searchJoiningClubIds(userId, ApprovalStatus.CONFIRMED);

        //then
        Assertions.assertThat(res1.size()).isEqualTo(0);
        Assertions.assertThat(res2.size()).isEqualTo(2);
    }

    @Test
    public void deleteMemberByClub_Test() throws Exception {
        //given

        //when
        Club clubPS = clubRepository.findById(club1Id).get();
        memberRepository.deleteMemberByClub(clubPS);
        List<Member> all = memberRepository.findAll();
        //then
        Assertions.assertThat(all.size()).isEqualTo(2);
    }


}