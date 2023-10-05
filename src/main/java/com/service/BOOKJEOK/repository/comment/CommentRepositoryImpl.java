package com.service.BOOKJEOK.repository.comment;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.service.BOOKJEOK.domain.Comment;
import com.service.BOOKJEOK.dto.comment.CommentResponseDto;
import com.service.BOOKJEOK.dto.comment.QCommentResponseDto_CommentSearchResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.service.BOOKJEOK.domain.QComment.comment;
import static com.service.BOOKJEOK.domain.QFeed.feed;
import static com.service.BOOKJEOK.domain.user.QUser.user;
import static com.service.BOOKJEOK.dto.comment.CommentResponseDto.*;

public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<CommentSearchResDto> searchCommentList(Long userId, Pageable pageable) {

        List<CommentSearchResDto> res = queryFactory
                .select(new QCommentResponseDto_CommentSearchResDto(
                        comment.feed.id,
                        comment.contents
                ))
                .from(comment)
                .where(comment.user.id.eq(userId))
                .orderBy(comment.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> query = queryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.user.id.eq(userId));

        return PageableExecutionUtils.getPage(res, pageable,
                query::fetchOne);
    }
}
