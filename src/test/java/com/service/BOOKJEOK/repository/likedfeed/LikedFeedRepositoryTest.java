package com.service.BOOKJEOK.repository.likedfeed;

import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.LikedFeed;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.dto.likedfeed.LikedFeedResponseDto;
import com.service.BOOKJEOK.repository.user.UserRepository;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import com.service.BOOKJEOK.repository.feed.FeedRepository;
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

import static com.service.BOOKJEOK.dto.feed.FeedResponseDto.*;
import static com.service.BOOKJEOK.dto.likedfeed.LikedFeedResponseDto.*;

@DataJpaTest
class LikedFeedRepositoryTest extends DummyObject {

    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private LikedFeedRepository likedFeedRepository;

    private Long userId;
    private Long clubId;
    private Long feedId;
    private Long likedFeedId;


    @BeforeEach
    public void Setup() {
        User me = newUser("mjh", "abc");
        User mePS = userRepository.save(me);
        Club myClub = newClub("club", mePS);
        Club clubPS = clubRepository.save(myClub);
        Feed feed = newFeed("feed", mePS, clubPS);
        Feed feedPS = feedRepository.save(feed);
        LikedFeed likedFeed = newLikedFeed(mePS, feedPS);
        LikedFeed likedFeedPS = likedFeedRepository.save(likedFeed);

        this.likedFeedId = likedFeedPS.getId();
        this.clubId = clubPS.getId();
        this.userId = mePS.getId();
        this.feedId = feedPS.getId();
    }

    @Test
    public void searchFeedList_Test() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0, 4);

        //when
        Page<FeedSearchResDto> res = likedFeedRepository.searchFeedList(userId, pageRequest);

        //then
        Assertions.assertThat(res.getTotalElements()).isEqualTo(1);
    }

    @Test
    public void searchFeedIdList_Test() throws Exception {
        //given

        //when
        List<LikedFeedIdResDto> res = likedFeedRepository.searchFeedIdList(userId);

        //then
        Assertions.assertThat(res.size()).isEqualTo(1);
    }
}