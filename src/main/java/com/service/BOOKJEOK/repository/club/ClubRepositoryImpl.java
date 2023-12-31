package com.service.BOOKJEOK.repository.club;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.service.BOOKJEOK.domain.club.*;
import com.service.BOOKJEOK.domain.member.QMember;
import com.service.BOOKJEOK.dto.club.ClubResponseDto;
import com.service.BOOKJEOK.dto.club.QClubResponseDto_ClubSearchResDto;
import com.service.BOOKJEOK.dto.comment.QCommentResponseDto_CommentSearchResDto;
import com.service.BOOKJEOK.dto.likedclub.QLikedClubResponseDto_LikedClubSearchResDto;
import com.service.BOOKJEOK.util.PathMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.service.BOOKJEOK.domain.QComment.comment;
import static com.service.BOOKJEOK.domain.QFeed.feed;
import static com.service.BOOKJEOK.domain.QLikedClub.likedClub;
import static com.service.BOOKJEOK.domain.club.QClub.*;
import static com.service.BOOKJEOK.domain.club.QClub.club;
import static com.service.BOOKJEOK.domain.club.QTagEntity.tagEntity;
import static com.service.BOOKJEOK.domain.member.QMember.member;
import static com.service.BOOKJEOK.dto.club.ClubRequestDto.*;

public class ClubRepositoryImpl implements ClubRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final QTagEntity tag = tagEntity;

    public ClubRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    /*
    (club - tag를 left join으로 연결해서 tag 조건에 맞는 클럽의 정보만 테이블에 뽑은 뒤에 중복을 제거하면 될 거 같은데 맞을까요?)
     */

    @Override
    public Page<Club> searchClub(ClubSearchReqDto clubSearchReqDto, Pageable pageable) {

        List<Club> result = queryFactory
                .select(club).distinct()
                .from(club)
                .join(club.tags, tag)
                .where(
                        tagsIn(clubSearchReqDto.getTags()), keywordIn(clubSearchReqDto.getKeyword())
                )
                .orderBy(sortBy(clubSearchReqDto.getSortBy()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> query = queryFactory
                .select(club.countDistinct())
                .from(club)
                .join(club.tags, tag)
                .where(
                        tagsIn(clubSearchReqDto.getTags()), keywordIn(clubSearchReqDto.getKeyword())
                ).distinct();

        return PageableExecutionUtils.getPage(result, pageable,
                query::fetchOne);
    }

    @Override
    public void deleteClub(Club club) {
        queryFactory.delete(member)
                .where(member.club.eq(club))
                .execute();

        queryFactory.delete(likedClub)
                .where(likedClub.club.eq(club))
                .execute();
    }

    @Override
    public List<ClubResponseDto.ClubSearchResDto> find4ClubList(String sortBy) {
        List<ClubResponseDto.ClubSearchResDto> res = queryFactory
                .select(new QClubResponseDto_ClubSearchResDto(
                        club.id,
                        club.title,
                        club.contents,
                        club.img_url
                ))
                .from(club)
                .orderBy(sortBy(sortBy))
                .limit(4)
                .fetch();

        return res;
    }

    private BooleanExpression keywordIn(String keyword) {
        if(keyword == null) return null;
        return club.title.contains(keyword);
    }

    private BooleanExpression tagsIn(String tags) {
        if(tags == null) return null;
        List<String> list = Arrays.asList(tags.split(","));
        List<Tag> tagList = list.stream().map(m ->
                Tag.of(m)).collect(Collectors.toList());

        return tag.tag.in(tagList);

    }

    private OrderSpecifier<?> sortBy(String sortBy) {
        if(sortBy.equals(PathMessage.LIKES)) return club.likes.asc();
        return club.createdAt.asc();
    }
}
