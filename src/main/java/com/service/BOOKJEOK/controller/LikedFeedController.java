package com.service.BOOKJEOK.controller;

import com.service.BOOKJEOK.dto.ResponseDto;
import com.service.BOOKJEOK.dto.likedfeed.LikedFeedRequestDto;
import com.service.BOOKJEOK.service.LikedFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.service.BOOKJEOK.dto.likedfeed.LikedFeedRequestDto.*;

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

}
