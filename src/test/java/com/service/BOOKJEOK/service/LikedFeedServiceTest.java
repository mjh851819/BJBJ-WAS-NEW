package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.LikedFeed;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.dto.likedfeed.LikedFeedRequestDto;
import com.service.BOOKJEOK.repository.UserRepository;
import com.service.BOOKJEOK.repository.feed.FeedRepository;
import com.service.BOOKJEOK.repository.likedfeed.LikedFeedRepository;
import com.service.BOOKJEOK.util.dummy.DummyObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.service.BOOKJEOK.dto.likedfeed.LikedFeedRequestDto.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikedFeedServiceTest extends DummyObject {

    @InjectMocks
    private LikedFeedService likedFeedService;

    @Mock
    private FeedRepository feedRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private LikedFeedRepository likedFeedRepository;

    @Test
    public void createLike() throws Exception {
        //given
        User user = newUser("mjh", "abc");
        Club club = newClub("club", user);
        Feed feed = newFeed("abc", user, club);
        LikedFeedCreateReqDto req = new LikedFeedCreateReqDto(1L, 1L);
        //LikedFeed likedFeed = newLikedFeed(user, feed);

        //stub
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(feedRepository.findById(any())).thenReturn(Optional.of(feed));

        //when
        //then
        likedFeedService.createLike(req);
    }

    @Test
    public void deleteLike_Test() throws Exception {
        //given
        User user = newUser("mjh", "abc");
        Club club = newClub("club", user);
        Feed feed = newFeed("abc", user, club);
        LikedFeed likedFeed = newLikedFeed(user, feed);

        //stub
        when(likedFeedRepository.findById(any())).thenReturn(Optional.of(likedFeed));

        //when
        //then
        likedFeedService.deleteLike(1L);
    }

}