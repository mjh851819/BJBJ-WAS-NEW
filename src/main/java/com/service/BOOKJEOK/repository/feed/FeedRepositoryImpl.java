package com.service.BOOKJEOK.repository.feed;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.dto.feed.FeedResponseDto;
import com.service.BOOKJEOK.dto.feed.QFeedResponseDto_FeedSearchDetailResDto;

import javax.persistence.EntityManager;

import java.util.List;

import static com.service.BOOKJEOK.domain.QComment.comment;
import static com.service.BOOKJEOK.domain.QFeed.feed;
import static com.service.BOOKJEOK.domain.club.QClub.club;
import static com.service.BOOKJEOK.domain.user.QUser.user;
import static com.service.BOOKJEOK.dto.feed.FeedResponseDto.*;

public class FeedRepositoryImpl implements FeedRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public FeedRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Feed findByIdDetail(Long feedId) {
        Feed res = queryFactory
                .select(feed)
                .from(feed)
                .leftJoin(feed.commentList, comment)
                .leftJoin(comment.user)
                .leftJoin(feed.user, user)
                .leftJoin(feed.club, club)
                .fetchJoin()
                .where(feed.id.eq(feedId))
                .fetchOne();

        return res;
    }
}
