package com.service.BOOKJEOK.repository;

import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.member.ApprovalStatus;
import com.service.BOOKJEOK.domain.member.Member;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import com.service.BOOKJEOK.util.dummy.DummyObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

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

}