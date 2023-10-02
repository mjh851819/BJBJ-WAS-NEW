package com.service.BOOKJEOK.repository.likedclub;

import com.service.BOOKJEOK.domain.LikedClub;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikedClubRepository extends JpaRepository<LikedClub, Long>, LikedClubRepositoryCustom {

    Optional<LikedClub> findByUserAndClub(User user, Club club);
}
