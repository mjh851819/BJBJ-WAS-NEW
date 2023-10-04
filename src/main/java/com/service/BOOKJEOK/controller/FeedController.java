package com.service.BOOKJEOK.controller;

import com.service.BOOKJEOK.dto.ResponseDto;
import com.service.BOOKJEOK.dto.feed.FeedRequestDto;
import com.service.BOOKJEOK.dto.feed.FeedResponseDto;
import com.service.BOOKJEOK.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.service.BOOKJEOK.dto.feed.FeedRequestDto.*;
import static com.service.BOOKJEOK.dto.feed.FeedResponseDto.*;

/*
- 게시글 등록
- 게시글 수정
- 게시글 삭제
- 게시글 상세정보 조회
- 피드 리스트 조회 (좋아요순 / 최신순)
- 클럽에 작성된 게시글 리스트 조회 (최신순)
- 내가 쓴 게시글 리스트 조회 (최신순)
 */
@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @PostMapping
    public ResponseEntity<?> createFeed(@RequestBody FeedCreateReqDto req, BindingResult bindingResult) {

        feedService.createFeed(req);

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 작성 성공", null), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateFeed(@RequestBody FeedUpdateReqDto req, BindingResult bindingResult) {

        feedService.updateFeed(req);

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 수정 성공", null), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFeed(@RequestParam("feedId") Long feedId) {

        feedService.deleteFeed(feedId);

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 삭제 성공", null), HttpStatus.OK);
    }

    @GetMapping("/{feedId}")
    public ResponseEntity<?> getFeedDetail(@PathVariable Long feedId) {

        FeedSearchDetailResDto res = feedService.searchFeed(feedId);

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 조회 성공", res), HttpStatus.OK);
    }

    @GetMapping("/clubs/{clubId}")
    public ResponseEntity<?> getClubFeedList(
            @PathVariable Long clubId,
            @PageableDefault(size = 4) Pageable pageable) {

        FeedSearchPageResDto res = feedService.searchFeedList(clubId, pageable);

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 리스트 조회 성공", res), HttpStatus.OK);
    }

}
