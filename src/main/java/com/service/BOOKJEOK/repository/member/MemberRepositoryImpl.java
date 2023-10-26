package com.service.BOOKJEOK.repository.member;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.member.ApprovalStatus;
import com.service.BOOKJEOK.dto.member.MemberResponseDto;
import com.service.BOOKJEOK.dto.member.QMemberResponseDto_MemberJoiningClubResDto;
import com.service.BOOKJEOK.dto.member.QMemberResponseDto_MemberJoiningClubsIdResDto;
import com.service.BOOKJEOK.dto.member.QMemberResponseDto_MemberSearchResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;

import java.util.List;

import static com.service.BOOKJEOK.domain.club.QClub.club;
import static com.service.BOOKJEOK.domain.member.QMember.member;
import static com.service.BOOKJEOK.domain.user.QUser.user;
import static com.service.BOOKJEOK.dto.member.MemberResponseDto.*;

public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<MemberSearchResDto> searchMember(Long clubId, ApprovalStatus status, Pageable pageable) {
        List<MemberSearchResDto> res = queryFactory
                .select(new QMemberResponseDto_MemberSearchResDto(
                        member.id,
                        member.club.id,
                        member.user,
                        member.status
                ))
                .from(member)
                .leftJoin(member.user, user)
                .where(member.club.id.eq(clubId), member.status.eq(status))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> query = queryFactory
                .select(member.count())
                .from(member)
                .leftJoin(member.user, user)
                .where(member.club.id.eq(clubId), member.status.eq(status));

        return PageableExecutionUtils.getPage(res, pageable,
                query::fetchOne);

    }

    @Override
    public Page<MemberJoiningClubResDto> searchJoiningClubs(Long userId, Pageable pageable) {
        List<MemberJoiningClubResDto> res = queryFactory
                .select(new QMemberResponseDto_MemberJoiningClubResDto(
                        club.id,
                        club.title,
                        club.img_url,
                        club.contents,
                        club.likes
                ))
                .from(member)
                .leftJoin(member.club, club)
                .where(member.user.id.eq(userId), member.status.eq(ApprovalStatus.CONFIRMED).or(member.status.eq(ApprovalStatus.MASTER)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> query = queryFactory
                .select(member.count())
                .from(member)
                .leftJoin(member.club, club)
                .where(member.user.id.eq(userId), member.status.eq(ApprovalStatus.CONFIRMED));

        return PageableExecutionUtils.getPage(res, pageable,
                query::fetchOne);
    }

    @Override
    public List<MemberJoiningClubsIdResDto> searchJoiningClubIds(Long userId) {
        List<MemberJoiningClubsIdResDto> res = queryFactory
                .select(new QMemberResponseDto_MemberJoiningClubsIdResDto(
                        member.user.id,
                        member.club.id,
                        member.status
                ))
                .from(member)
                .where(member.user.id.eq(userId))
                .fetch();

        return res;
    }

    @Override
    public void deleteMemberByClub(Club club) {
        queryFactory
                .delete(member)
                .where(member.club.eq(club))
                .execute();
    }
}
