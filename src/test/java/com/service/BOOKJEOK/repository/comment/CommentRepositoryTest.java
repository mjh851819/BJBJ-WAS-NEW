package com.service.BOOKJEOK.repository.comment;

import com.service.BOOKJEOK.domain.Comment;
import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
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

import java.time.LocalDateTime;

import static com.service.BOOKJEOK.dto.comment.CommentResponseDto.*;

@DataJpaTest
class CommentRepositoryTest extends DummyObject {

    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private CommentRepository commentRepository;

    private Long userId;
    private Long clubId;
    private Long feedId;


    @BeforeEach
    public void Setup() {
        User me = newUser("mjh", "abc");
        User mePS = userRepository.save(me);
        Club myClub = newClub("club", mePS);
        Club clubPS = clubRepository.save(myClub);
        Feed feed = newFeed("feed", mePS, clubPS);
        Feed feedPS = feedRepository.save(feed);
        Comment comment1 = Comment.builder()
                .contents("contents1")
                .user(mePS)
                .feed(feedPS)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        Comment comment2 = Comment.builder()
                .contents("contents2")
                .user(mePS)
                .feed(feedPS)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        commentRepository.save(comment1);
        commentRepository.save(comment2);

        this.clubId = clubPS.getId();
        this.userId = mePS.getId();
        this.feedId = feedPS.getId();
    }

    @Test
    public void searchCommentList_Test() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0, 4);
        //when
        Page<CommentSearchResDto> res = commentRepository.searchCommentList(userId, pageRequest);
        //then
        Assertions.assertThat(res.getTotalElements()).isEqualTo(2);
    }

    @Test
    public void searchCommentListByFeedId() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0, 4);

        //when
        Page<CommentSearchResDto> res = commentRepository.searchCommentList(feedId, pageRequest);

        //then
        Assertions.assertThat(res.getTotalElements()).isEqualTo(2);
    }

}