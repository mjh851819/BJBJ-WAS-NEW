package com.service.BOOKJEOK.repository.likedfeed;

import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.LikedClub;
import com.service.BOOKJEOK.domain.LikedFeed;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikedFeedRepository extends JpaRepository<LikedFeed, Long>, LikedFeedRepositoryCustom {

    Optional<LikedFeed> findByUserAndFeed(User user, Feed feed);
}
