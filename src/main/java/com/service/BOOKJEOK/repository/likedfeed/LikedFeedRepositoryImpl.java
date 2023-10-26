package com.service.BOOKJEOK.repository.likedfeed;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.service.BOOKJEOK.domain.LikedFeed;
import com.service.BOOKJEOK.domain.QLikedFeed;
import com.service.BOOKJEOK.dto.feed.FeedResponseDto;
import com.service.BOOKJEOK.dto.feed.QFeedResponseDto_FeedSearchResDto;
import com.service.BOOKJEOK.dto.likedfeed.LikedFeedResponseDto;
import com.service.BOOKJEOK.dto.likedfeed.QLikedFeedResponseDto_LikedFeedIdResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static com.service.BOOKJEOK.domain.QFeed.feed;
import static com.service.BOOKJEOK.domain.QLikedFeed.*;
import static com.service.BOOKJEOK.domain.user.QUser.user;
import static com.service.BOOKJEOK.dto.feed.FeedResponseDto.*;
import static com.service.BOOKJEOK.dto.likedfeed.LikedFeedResponseDto.*;

public class LikedFeedRepositoryImpl implements LikedFeedRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public LikedFeedRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<FeedSearchResDto> searchFeedList(Long userId, Pageable pageable) {
        List<FeedSearchResDto> res = queryFactory
                .select(new QFeedResponseDto_FeedSearchResDto(
                        user,
                        feed.id,
                        feed.contents,
                        feed.likes,
                        feed.commentList.size()
                ))
                .from(likedFeed)
                .leftJoin(likedFeed.feed, feed)
                .leftJoin(likedFeed.user, user)
                .where(likedFeed.user.id.eq(userId))
                .orderBy(likedFeed.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> query = queryFactory
                .select(likedFeed.count())
                .from(likedFeed)
                .where(likedFeed.user.id.eq(userId));

        return PageableExecutionUtils.getPage(res, pageable,
                query::fetchOne);
    }

    @Override
    public List<LikedFeedIdResDto> searchFeedIdList(Long userId) {
        List<LikedFeedIdResDto> res = queryFactory
                .select(new QLikedFeedResponseDto_LikedFeedIdResDto(likedFeed.feed.id))
                .from(likedFeed)
                .where(likedFeed.user.id.eq(userId))
                .fetch();

        return res;
    }
}
