package com.service.BOOKJEOK.repository.feed;

import com.service.BOOKJEOK.domain.Comment;
import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.dto.feed.FeedResponseDto;
import com.service.BOOKJEOK.repository.UserRepository;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import com.service.BOOKJEOK.repository.comment.CommentRepository;
import com.service.BOOKJEOK.util.dummy.DummyObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.service.BOOKJEOK.dto.feed.FeedResponseDto.*;
import static org.junit.jupiter.api.Assertions.*;

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


    @Test
    public void delete_Test() throws Exception {
        //given
        User me = newUser("mjh", "abc");
        User mePS = userRepository.save(me);
        Club myClub = newClub("club", mePS);
        Club clubPS = clubRepository.save(myClub);
        Feed feed = newFeed("feed", mePS, clubPS);
        Feed feedPS = feedRepository.save(feed);
        Comment comment = Comment.builder()
                .contents("contents")
                .user(mePS)
                .feed(feedPS)
                .build();
        commentRepository.save(comment);

        //when
        //then
        Assertions.assertThat(commentRepository.findAll().size()).isEqualTo(1);
        Assertions.assertThat(feedRepository.findAll().size()).isEqualTo(1);
        feedRepository.delete(feedPS);
        Assertions.assertThat(commentRepository.findAll().size()).isEqualTo(0);
        Assertions.assertThat(feedRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    public void findByIdDetail_Test() throws Exception {
        //given
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
                .build();
        Comment comment2 = Comment.builder()
                .contents("contents2")
                .user(mePS)
                .feed(feedPS)
                .build();

        //when
        Feed res = feedRepository.findByIdDetail(mePS.getId());
        FeedSearchDetailResDto dto = new FeedSearchDetailResDto(res);
        System.out.println("테스트 : " + dto.toString());

        //then
    }
}