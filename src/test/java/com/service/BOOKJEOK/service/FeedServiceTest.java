package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.Comment;
import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.repository.user.UserRepository;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import com.service.BOOKJEOK.repository.feed.FeedRepository;
import com.service.BOOKJEOK.util.PathMessage;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.service.BOOKJEOK.dto.feed.FeedRequestDto.*;
import static com.service.BOOKJEOK.dto.feed.FeedResponseDto.*;
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

    @Test
    public void searchFeed() throws Exception {
        //given
        User me = newUser("mjh", "abc");
        Club myClub = newClub("club", me);
        Feed feed = newFeed("feed", me, myClub);
        Comment comment1 = Comment.builder()
                .contents("contents1")
                .user(me)
                .feed(feed)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        Comment comment2 = Comment.builder()
                .contents("contents2")
                .user(me)
                .feed(feed)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        //stub
        when(feedRepository.findByIdDetail(any())).thenReturn(feed);
        //when
        FeedSearchDetailResDto res = feedService.searchFeed(1L);
        System.out.println(res);

        //then
        Assertions.assertThat(res.getComments().size()).isEqualTo(2);
        Assertions.assertThat(res.getTitle()).isEqualTo(feed.getTitle());
    }

    @Test
    public void searchClubFeedList_Test() throws Exception {
        //given
        Long clubId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 4);

        //stub
        List<FeedSearchResDto> list = new ArrayList<>();
        list.add(new FeedSearchResDto(1L, "contents", 0, 0));
        Page<FeedSearchResDto> tar = new PageImpl<>(list, pageRequest, 1);
        when(feedRepository.findClubFeedList(any(), any(), any())).thenReturn(tar);

        //when
        FeedSearchPageResDto res = feedService.searchClubFeedList(clubId, PathMessage.CREATED_AT, pageRequest);

        //then
        Assertions.assertThat(res.getTotalCount()).isEqualTo(1);
    }

    @Test
    public void searchFeedList_Test() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0, 4);

        //stub
        List<FeedSearchResDto> list = new ArrayList<>();
        list.add(new FeedSearchResDto(1L, "contents", 0, 0));
        Page<FeedSearchResDto> tar = new PageImpl<>(list, pageRequest, 1);
        when(feedRepository.findFeedList(any(), any())).thenReturn(tar);

        //when
        FeedSearchPageResDto res = feedService.searchFeedList(PathMessage.CREATED_AT, pageRequest);

        //then
        Assertions.assertThat(res.getTotalCount()).isEqualTo(1);
    }

    @Test
    public void searchUserFeedList_Test() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0, 4);

        //stub
        List<FeedSearchResDto> list = new ArrayList<>();
        list.add(new FeedSearchResDto(1L, "contents", 0, 0));
        Page<FeedSearchResDto> tar = new PageImpl<>(list, pageRequest, 1);
        when(feedRepository.findUserFeedList(any(), any(), any())).thenReturn(tar);

        //when
        FeedSearchPageResDto res = feedService.searchUserFeedList(1L, PathMessage.CREATED_AT, pageRequest);

        //then
        Assertions.assertThat(res.getTotalCount()).isEqualTo(1);
    }

}