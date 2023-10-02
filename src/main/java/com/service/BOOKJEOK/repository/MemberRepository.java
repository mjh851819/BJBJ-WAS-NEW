package com.service.BOOKJEOK.repository;

import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.member.Member;
import com.service.BOOKJEOK.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserAndClub(User user, Club club);
}
