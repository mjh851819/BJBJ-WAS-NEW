package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.Comment;
import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.dto.comment.CommentRequestDto;
import com.service.BOOKJEOK.handler.ex.CustomApiException;
import com.service.BOOKJEOK.handler.ex.ExMessage;
import com.service.BOOKJEOK.repository.UserRepository;
import com.service.BOOKJEOK.repository.comment.CommentRepository;
import com.service.BOOKJEOK.repository.feed.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.service.BOOKJEOK.dto.comment.CommentRequestDto.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;

    public void createComment(CommentCreateReqDto req) {
        User userPS = userRepository.findById(req.getUserId()).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_USER));
        Feed feedPS = feedRepository.findById(req.getFeedId()).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_FEED));

        Comment comment = req.toEntity(userPS, feedPS);
        commentRepository.save(comment);
    }
}
