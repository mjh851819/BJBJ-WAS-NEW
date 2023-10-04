package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.dto.feed.FeedRequestDto;
import com.service.BOOKJEOK.repository.UserRepository;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import com.service.BOOKJEOK.repository.feed.FeedRepository;
import com.service.BOOKJEOK.util.dummy.DummyObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.service.BOOKJEOK.dto.feed.FeedRequestDto.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedServiceTest extends DummyObject {

    @InjectMocks
    private FeedService feedService;

    @Mock
    private FeedRepository feedRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ClubRepository clubRepository;

    @Test
    public void createFeed_Test() throws Exception {
        //given
        User me = newUser("mjh", "abc");
        Club myClub = newClub("club", me);
        //Feed myFeed = newFeed("myFeed", me, myClub);
        FeedCreateReqDto res = new FeedCreateReqDto(1L, 1L, "feed", "contents", "url");

        //stub
        when(userRepository.findById(any())).thenReturn(Optional.of(me));
        when(clubRepository.findById(any())).thenReturn(Optional.of(myClub));

        //when
        //then
        feedService.createFeed(res);
    }

    @Test
    public void updateFeed_Test() throws Exception {
        //given
        User me = newUser("mjh", "abc");
        Club myClub = newClub("club", me);
        Feed myFeed = newFeed("myFeed", me, myClub);
        FeedUpdateReqDto req = new FeedUpdateReqDto(1L, 1L, 1L, "abc", "abc", "abc");

        //stub
        when(userRepository.findById(any())).thenReturn(Optional.of(me));
        when(clubRepository.findById(any())).thenReturn(Optional.of(myClub));
        when(feedRepository.findById(any())).thenReturn(Optional.of(myFeed));

        //when
        feedService.updateFeed(req);

        //then
        Assertions.assertThat(myFeed.getTitle()).isEqualTo("abc");
    }

    @Test
    public void deleteFeed_Test() throws Exception {
        //given
        Long feedId = 1L;
        Feed exFeed = newFeed("abc", null, null);

        //stub
        when(feedRepository.findById(any())).thenReturn(Optional.of(exFeed));

        //when
        //then
        feedService.deleteFeed(feedId);
    }

}