package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.LikedFeed;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.repository.user.UserRepository;
import com.service.BOOKJEOK.repository.feed.FeedRepository;
import com.service.BOOKJEOK.repository.likedfeed.LikedFeedRepository;
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

import static com.service.BOOKJEOK.dto.feed.FeedResponseDto.*;
import static com.service.BOOKJEOK.dto.likedfeed.LikedFeedRequestDto.*;
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

    @Test
    public void searchFeedList_Test() throws Exception {
        //given
        Long userID = 1L;
        PageRequest pageRequest = PageRequest.of(0, 4);

        //stub
        User user = newUser("mjh", "abc");
        List<FeedSearchResDto> list = new ArrayList<>();
        list.add(new FeedSearchResDto(user, 1L, "contents", 0, 0));
        Page<FeedSearchResDto> tar = new PageImpl<>(list, pageRequest, 1);
        when(likedFeedRepository.searchFeedList(any(), any())).thenReturn(tar);

        //when
        FeedSearchPageResDto res = likedFeedService.searchFeedList(userID, pageRequest);

        //then
        Assertions.assertThat(res.getTotalCount()).isEqualTo(1);
    }

}