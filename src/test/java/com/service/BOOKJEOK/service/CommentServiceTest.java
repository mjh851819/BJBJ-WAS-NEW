package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.Comment;
import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.dto.comment.CommentRequestDto;
import com.service.BOOKJEOK.repository.UserRepository;
import com.service.BOOKJEOK.repository.comment.CommentRepository;
import com.service.BOOKJEOK.repository.feed.FeedRepository;
import com.service.BOOKJEOK.util.dummy.DummyObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.service.BOOKJEOK.dto.comment.CommentRequestDto.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest extends DummyObject {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private FeedRepository feedRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;

    @Test
    public void createComment_Test() throws Exception {
        //given
        CommentCreateReqDto req = new CommentCreateReqDto(1L, 1L, "abc");

        //stub
        User me = newUser("mjh", "abc");
        when(userRepository.findById(any())).thenReturn(Optional.of(me));
        Club myClub = newClub("club", me);
        Feed feed = newFeed("title", me, myClub);
        when(feedRepository.findById(any())).thenReturn(Optional.of(feed));

        //when
        //then
        commentService.createComment(req);
    }

    @Test
    public void updateComment_Test() throws Exception {
        //given
        CommentUpdateReqDto req = new CommentUpdateReqDto(1L, 1L, 1L, "abc");

        //stub
        User me = newUser("mjh", "abc");
        when(userRepository.findById(any())).thenReturn(Optional.of(me));
        Club myClub = newClub("club", me);
        Feed feed = newFeed("title", me, myClub);
        when(feedRepository.findById(any())).thenReturn(Optional.of(feed));
        Comment comment = newComment("abc", me, feed);
        when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

        //when
        //then
        commentService.updateComment(req);
    }

}