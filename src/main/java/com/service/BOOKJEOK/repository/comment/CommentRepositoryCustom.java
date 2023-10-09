package com.service.BOOKJEOK.repository.comment;

import com.service.BOOKJEOK.domain.Comment;
import com.service.BOOKJEOK.dto.comment.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.service.BOOKJEOK.dto.comment.CommentResponseDto.*;

public interface CommentRepositoryCustom {
    Page<CommentSearchResDto> searchCommentList(Long userId, Pageable pageable);

    void deleteByFeedIds(List<Long> ids);

    Page<CommentDetailResDto> searchCommentListByFeedId(Long feedId, Pageable pageable);
}
