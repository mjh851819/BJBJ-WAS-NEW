package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.Comment;
import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.repository.user.UserRepository;
import com.service.BOOKJEOK.repository.comment.CommentRepository;
import com.service.BOOKJEOK.repository.feed.FeedRepository;
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

import static com.service.BOOKJEOK.dto.comment.CommentRequestDto.*;
import static com.service.BOOKJEOK.dto.comment.CommentResponseDto.*;
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

    @Test
    public void deleteComment_Test() throws Exception {
        //given
        Long commentId = 1L;

        //stub
        User me = newUser("mjh", "abc");
        Club myClub = newClub("club", me);
        Feed feed = newFeed("title", me, myClub);
        Comment comment = newComment("abc", me, feed);
        when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

        //when
        //then
        commentService.deleteComment(1L);
    }

    @Test
    public void searchCommentList_Test() throws Exception {
        //given
        Long userId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 4);

        //stub
        List<CommentSearchResDto> list = new ArrayList<>();
        list.add(new CommentSearchResDto(1L, "abc"));
        list.add(new CommentSearchResDto(2L, "abc"));
        Page<CommentSearchResDto> tar = new PageImpl<>(list, pageRequest, 2);
        when(commentRepository.searchCommentList(any(), any())).thenReturn(tar);

        //when
        CommentSearchPageResDto res = commentService.searchCommentList(userId, pageRequest);

        //then
        Assertions.assertThat(res.getTotalCount()).isEqualTo(2);

    }

    @Test
    public void searchDetailCommentList_Test() throws Exception {
        //given
        Long feedId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 4);

        //stub
        List<CommentDetailResDto> list = new ArrayList<>();
        list.add(new CommentDetailResDto(1L, "min", "qwert", 1L, "con"));
        list.add(new CommentDetailResDto(2L, "min", "qwert", 2L, "con"));
        Page<CommentDetailResDto> tar = new PageImpl<>(list, pageRequest, 2);
        when(commentRepository.searchCommentListByFeedId(any(), any())).thenReturn(tar);

        //when
        CommentDetailPageResDto res = commentService.searchDetailCommentList(feedId, pageRequest);
        //then
        Assertions.assertThat(res.getTotalCount()).isEqualTo(2);

    }
}