package com.service.BOOKJEOK.repository.feed;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.dto.feed.FeedResponseDto;
import com.service.BOOKJEOK.dto.feed.QFeedResponseDto_FeedSearchDetailResDto;
import com.service.BOOKJEOK.dto.feed.QFeedResponseDto_FeedSearchResDto;
import com.service.BOOKJEOK.util.PathMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

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

    @Override
    public Page<FeedSearchResDto> findClubFeedList(Long clubId, Pageable pageable) {
        List<FeedSearchResDto> res = queryFactory
                .select(new QFeedResponseDto_FeedSearchResDto(
                        feed.id,
                        feed.contents,
                        feed.likes,
                        feed.commentList.size()
                ))
                .from(feed)
                .leftJoin(feed.club, club)
                .leftJoin(feed.commentList, comment)
                .where(club.id.eq(clubId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> query = queryFactory
                .select(feed.count())
                .from(feed)
                .leftJoin(feed.club, club)
                .where(club.id.eq(clubId));

        return PageableExecutionUtils.getPage(res, pageable,
                query::fetchOne);
    }

    @Override
    public Page<FeedSearchResDto> findFeedList(String sortBy, Pageable pageable) {
        List<FeedSearchResDto> res = queryFactory
                .select(new QFeedResponseDto_FeedSearchResDto(
                        feed.id,
                        feed.contents,
                        feed.likes,
                        feed.commentList.size()
                ))
                .from(feed)
                .leftJoin(feed.commentList, comment)
                .orderBy(sort(sortBy))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> query = queryFactory
                .select(feed.count())
                .from(feed);

        return PageableExecutionUtils.getPage(res, pageable,
                query::fetchOne);
    }

    private OrderSpecifier<?> sort(String sortBy) {
        if(sortBy.equals(PathMessage.LIKES)) return feed.likes.asc();
        else return feed.createdAt.asc();
    }
}
