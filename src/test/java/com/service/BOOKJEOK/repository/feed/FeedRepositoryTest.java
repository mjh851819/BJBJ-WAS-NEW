package com.service.BOOKJEOK.repository.feed;

import com.service.BOOKJEOK.domain.Comment;
import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.LikedFeed;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.repository.UserRepository;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import com.service.BOOKJEOK.repository.comment.CommentRepository;
import com.service.BOOKJEOK.repository.likedfeed.LikedFeedRepository;
import com.service.BOOKJEOK.util.PathMessage;
import com.service.BOOKJEOK.util.dummy.DummyObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static com.service.BOOKJEOK.dto.feed.FeedResponseDto.*;

@DataJpaTest
class FeedRepositoryTest extends DummyObject {
    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private LikedFeedRepository likedFeedRepository;

    private Long userId;
    private Long clubId;
    private Long feedId;


    @BeforeEach
    public void Setup() {
        User me = newUser("mjh", "abc");
        User mePS = userRepository.save(me);
        Club myClub = newClub("club", mePS);
        Club clubPS = clubRepository.save(myClub);
        Feed feed1 = newFeed("feed1", mePS, clubPS);
        Feed feed2 = newFeed("feed2", mePS, clubPS);
        Feed feedPS1 = feedRepository.save(feed1);
        Feed feedPS2 = feedRepository.save(feed2);
        Comment comment1 = Comment.builder()
                .contents("contents1")
                .user(mePS)
                .feed(feedPS1)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        Comment comment2 = Comment.builder()
                .contents("contents2")
                .user(mePS)
                .feed(feedPS2)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        LikedFeed likedFeed1 = newLikedFeed(mePS, feedPS1);
        LikedFeed likedFeed2 = newLikedFeed(mePS, feedPS2);
        likedFeedRepository.save(likedFeed1);
        likedFeedRepository.save(likedFeed2);


        this.clubId = clubPS.getId();
        this.userId = mePS.getId();
        this.feedId = feedPS1.getId();
    }

    /*@Test
    public void delete_Test() throws Exception {
        //given
        Feed feedPS = feedRepository.findById(feedId).get();

        //when
        //then
        Assertions.assertThat(commentRepository.findAll().size()).isEqualTo(2);
        Assertions.assertThat(feedRepository.findAll().size()).isEqualTo(1);
        feedRepository.delete(feedPS);
        Assertions.assertThat(commentRepository.findAll().size()).isEqualTo(0);
        Assertions.assertThat(feedRepository.findAll().size()).isEqualTo(0);
    }*/

    @Test
    public void findByIdDetail_Test() throws Exception {
        //given
        //when
        Feed res = feedRepository.findByIdDetail(feedId);
        FeedSearchDetailResDto dto = new FeedSearchDetailResDto(res);
        System.out.println("테스트 : " + dto.toString());

        //then
    }

    @Test
    public void findClubFeedList_Test() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0, 4);
        //when
        Page<FeedSearchResDto> res = feedRepository.findClubFeedList(clubId, PathMessage.CREATED_AT, pageRequest);
        //then
        Assertions.assertThat(res.getTotalElements()).isEqualTo(2);
    }

    @Test
    public void findFeedList_Test() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0, 4);

        //when
        Page<FeedSearchResDto> res = feedRepository.findFeedList(PathMessage.CREATED_AT, pageRequest);
        //then
        Assertions.assertThat(res.getTotalElements()).isEqualTo(2);
    }

    @Test
    public void findUserFeedList_Test() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0, 4);

        //when
        Page<FeedSearchResDto> res = feedRepository.findUserFeedList(userId, PathMessage.CREATED_AT, pageRequest);

        //then
        Assertions.assertThat(res.getTotalElements()).isEqualTo(2);
    }

    @Test
    public void deleteByFeedIds_Test() throws Exception {
        //given
        Club club = clubRepository.findById(clubId).get();
        List<Long> ids = feedRepository.findIdsByClub(club);
        //when
        List<Feed> beforeFeed = feedRepository.findAll();
        List<Comment> beforeComment = commentRepository.findAll();
        List<LikedFeed> beforeLike = likedFeedRepository.findAll();
        Assertions.assertThat(beforeLike.size()).isEqualTo(2);
        Assertions.assertThat(beforeFeed.size()).isEqualTo(2);
        Assertions.assertThat(beforeComment.size()).isEqualTo(2);
        feedRepository.deleteByFeedIds(ids);
        List<Feed> after = feedRepository.findAll();
        List<Comment> afterComment = commentRepository.findAll();
        List<LikedFeed> afterLike = likedFeedRepository.findAll();
        //then
        Assertions.assertThat(after.size()).isEqualTo(0);
        Assertions.assertThat(afterComment.size()).isEqualTo(0);
        Assertions.assertThat(afterLike.size()).isEqualTo(0);
    }

    @Test
    public void deleteFeedById_Test() throws Exception {
        //given
        List<Comment> beforeC = commentRepository.findAll();
        List<LikedFeed> beforeL = likedFeedRepository.findAll();
        List<Feed> beforeF = feedRepository.findAll();
        Assertions.assertThat(beforeC.size()).isEqualTo(2);
        Assertions.assertThat(beforeF.size()).isEqualTo(2);
        Assertions.assertThat(beforeL.size()).isEqualTo(2);

        //when
        feedRepository.deleteFeedById(feedId);
        List<Comment> afterC = commentRepository.findAll();
        List<LikedFeed> afterL = likedFeedRepository.findAll();
        List<Feed> afterF = feedRepository.findAll();

        //then
        Assertions.assertThat(afterC.size()).isEqualTo(1);
        Assertions.assertThat(afterL.size()).isEqualTo(1);
        Assertions.assertThat(afterF.size()).isEqualTo(1);
    }
}