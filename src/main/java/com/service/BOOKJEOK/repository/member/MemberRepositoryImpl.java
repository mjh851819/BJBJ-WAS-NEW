package com.service.BOOKJEOK.repository.member;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.service.BOOKJEOK.domain.member.ApprovalStatus;
import com.service.BOOKJEOK.dto.member.MemberResponseDto;
import com.service.BOOKJEOK.dto.member.QMemberResponseDto_MemberSearchResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;

import java.util.List;

import static com.service.BOOKJEOK.domain.member.QMember.member;
import static com.service.BOOKJEOK.domain.user.QUser.user;

public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<MemberResponseDto.MemberSearchResDto> searchMember(Long userId, ApprovalStatus status, Pageable pageable) {
        List<MemberResponseDto.MemberSearchResDto> res = queryFactory
                .select(new QMemberResponseDto_MemberSearchResDto(
                        member.id,
                        member.club.id,
                        user.id,
                        user.img_url,
                        user.name,
                        user.email,
                        member.status
                ))
                .from(member)
                .leftJoin(member.user, user)
                .where(member.user.id.eq(userId), member.status.eq(status))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> query = queryFactory
                .select(member.count())
                .from(member)
                .leftJoin(member.user, user)
                .where(member.user.id.eq(userId), member.status.eq(status));

        return PageableExecutionUtils.getPage(res, pageable,
                query::fetchOne);

    }
}
