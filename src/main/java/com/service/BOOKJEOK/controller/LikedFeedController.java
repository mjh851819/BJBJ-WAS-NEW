package com.service.BOOKJEOK.controller;

import com.service.BOOKJEOK.dto.ResponseDto;
import com.service.BOOKJEOK.dto.feed.FeedResponseDto;
import com.service.BOOKJEOK.dto.likedfeed.LikedFeedRequestDto;
import com.service.BOOKJEOK.dto.likedfeed.LikedFeedResponseDto;
import com.service.BOOKJEOK.dto.member.MemberResponseDto;
import com.service.BOOKJEOK.service.LikedFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.service.BOOKJEOK.dto.feed.FeedResponseDto.*;
import static com.service.BOOKJEOK.dto.likedfeed.LikedFeedRequestDto.*;
import static com.service.BOOKJEOK.dto.likedfeed.LikedFeedResponseDto.*;

/*
- 피드 좋아요 신청
- 피드 좋아요 취소
- 좋아요한 피드 리스트 검색
 */

@RestController
@RequestMapping("/likedfeeds")
@RequiredArgsConstructor
public class LikedFeedController {

    private final LikedFeedService likedFeedService;

    @PostMapping
    public ResponseEntity<?> createLike(@RequestBody LikedFeedCreateReqDto req) {

        likedFeedService.createLike(req);

        return new ResponseEntity<>(new ResponseDto<>(1, "좋아요 신청 성공", null), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteLike(@RequestParam("feedId") Long feedId, @RequestParam("userId") Long userId) {

        likedFeedService.deleteLike(feedId, userId);

        return new ResponseEntity<>(new ResponseDto<>(1, "좋아요 삭제 성공", null), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> searchFeedList(
            @PathVariable("userId") Long userId,
            @PageableDefault(size = 4) Pageable pageable) {

        FeedSearchPageResDto res = likedFeedService.searchFeedList(userId, pageable);

        return new ResponseEntity<>(new ResponseDto<>(1, "좋아요한 피드 리스트 조회 성공", res), HttpStatus.OK);
    }

    //참여중인 & 가입 대기중인 독서모임 id만 조회
    @GetMapping("/ids")
    public ResponseEntity<?> getLikedFeedIds(
            @RequestParam("userId") Long userId) {

        LikedFeedIdListResDto res = likedFeedService.getLikedFeedIds(userId);

        return new ResponseEntity<>(new ResponseDto<>(1, "좋아요한 피드 id 리스트 조회 성공 ", res), HttpStatus.OK);
    }

}
