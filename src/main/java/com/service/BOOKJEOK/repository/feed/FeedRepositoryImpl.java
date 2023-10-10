package com.service.BOOKJEOK.repository.feed;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.club.Club;
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
import static com.service.BOOKJEOK.domain.QLikedFeed.likedFeed;
import static com.service.BOOKJEOK.domain.club.QClub.club;
import static com.service.BOOKJEOK.domain.user.QUser.user;
import static com.service.BOOKJEOK.dto.feed.FeedResponseDto.*;

public class FeedRepositoryImpl implements FeedRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public FeedRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Feed findByIdDetail(Long feedId) {
        Feed res = queryFactory
                .select(feed)
                .from(feed)
                .leftJoin(feed.user, user)
                .leftJoin(feed.club, club)
                .fetchJoin()
                .where(feed.id.eq(feedId))
                .fetchOne();

        return res;
    }

    @Override
    public Page<FeedSearchResDto> findClubFeedList(Long clubId, String sortBy, Pageable pageable) {
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
                .orderBy(sort(sortBy))
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

    @Override
    public Page<FeedSearchResDto> findUserFeedList(Long userId, String sortBy, Pageable pageable) {
        List<FeedSearchResDto> res = queryFactory
                .select(new QFeedResponseDto_FeedSearchResDto(
                        feed.id,
                        feed.contents,
                        feed.likes,
                        feed.commentList.size()
                ))
                .from(feed)
                .leftJoin(feed.user, user)
                .leftJoin(feed.commentList, comment)
                .where(user.id.eq(userId))
                .orderBy(sort(sortBy))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> query = queryFactory
                .select(feed.count())
                .from(feed)
                .leftJoin(feed.user, user)
                .where(user.id.eq(userId));

        return PageableExecutionUtils.getPage(res, pageable,
                query::fetchOne);
    }

    @Override
    public List<Long> findIdsByClub(Club club) {
        List<Long> res = queryFactory
                .select(feed.id)
                .from(feed)
                .where(feed.club.eq(club))
                .fetch();

        return res;
    }

    @Override
    public void deleteByFeedIds(List<Long> ids) {
        queryFactory
                .delete(comment)
                        .where(comment.feed.id.in(ids))
                                .execute();
        queryFactory
                .delete(likedFeed)
                .where(likedFeed.feed.id.in(ids))
                .execute();

        queryFactory
                .delete(feed)
                .where(feed.id.in(ids))
                .execute();
    }

    @Override
    public void deleteFeedById(Long feedId) {
        queryFactory
                .delete(likedFeed)
                .where(likedFeed.feed.id.eq(feedId))
                .execute();
        queryFactory
                .delete(comment)
                .where(comment.feed.id.eq(feedId))
                .execute();
        queryFactory
                .delete(feed)
                .where(feed.id.eq(feedId))
                .execute();
    }

    @Override
    public List<FeedSearchResDto> find4FeedList(String sortBy) {
        List<FeedSearchResDto> res = queryFactory
                .select(new QFeedResponseDto_FeedSearchResDto(
                        feed.id,
                        feed.contents,
                        feed.likes,
                        feed.commentList.size()
                ))
                .distinct()
                .from(feed)
                .leftJoin(feed.commentList, comment)
                .orderBy(sort(sortBy))
                .limit(4)
                .fetch();

        return res;
    }

    private OrderSpecifier<?> sort(String sortBy) {
        if(sortBy.equals(PathMessage.LIKES)) return feed.likes.asc();
        else return feed.createdAt.asc();
    }
}
