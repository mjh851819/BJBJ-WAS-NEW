package com.service.BOOKJEOK.repository.likedclub;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.service.BOOKJEOK.domain.QLikedClub;
import com.service.BOOKJEOK.dto.likedclub.LikedClubResponseDto;
import com.service.BOOKJEOK.dto.likedclub.QLikedClubResponseDto_LikedClubIdResDto;
import com.service.BOOKJEOK.dto.likedclub.QLikedClubResponseDto_LikedClubSearchResDto;
import com.service.BOOKJEOK.dto.member.MemberResponseDto;
import com.service.BOOKJEOK.dto.member.QMemberResponseDto_MemberJoiningClubsIdResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.service.BOOKJEOK.domain.QLikedClub.likedClub;
import static com.service.BOOKJEOK.domain.club.QClub.club;
import static com.service.BOOKJEOK.dto.likedclub.LikedClubResponseDto.*;
import static com.service.BOOKJEOK.dto.member.MemberResponseDto.*;

public class LikedClubRepositoryImpl implements LikedClubRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public LikedClubRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<LikedClubSearchResDto> searchClubList(Long userId, Pageable pageable) {
        List<LikedClubSearchResDto> res = queryFactory
                .select(new QLikedClubResponseDto_LikedClubSearchResDto(
                        likedClub.club.id,
                        club.title,
                        club.contents,
                        club.img_url,
                        club.likes
                ))
                .from(likedClub)
                .leftJoin(likedClub.club, club)
                .where(likedClub.user.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> query = queryFactory
                .select(likedClub.count())
                .from(likedClub)
                .leftJoin(likedClub.club, club)
                .where(likedClub.user.id.eq(userId));

        return PageableExecutionUtils.getPage(res, pageable,
                query::fetchOne);
    }

    @Override
    public List<LikedClubIdResDto> searchClubIdList(Long userId) {
        List<LikedClubIdResDto> res = queryFactory
                .select(new QLikedClubResponseDto_LikedClubIdResDto(likedClub.club.id))
                .from(likedClub)
                .where(likedClub.user.id.eq(userId))
                .fetch();

        return res;
    }
}
