package com.service.BOOKJEOK.controller;

import com.service.BOOKJEOK.dto.ResponseDto;
import com.service.BOOKJEOK.dto.likedclub.LikedClubRequestDto;
import com.service.BOOKJEOK.dto.likedclub.LikedClubResponseDto;
import com.service.BOOKJEOK.service.LikedClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.service.BOOKJEOK.dto.likedclub.LikedClubRequestDto.*;
import static com.service.BOOKJEOK.dto.likedclub.LikedClubResponseDto.*;

@RestController
@RequestMapping("/likedclubs")
@RequiredArgsConstructor
public class LikedClubController {

    private final LikedClubService likedClubService;

    //좋아요 신청
    @PostMapping
    public ResponseEntity<?> createLikedClub(
            @RequestBody LikedClubCreateReqDto likedClubRequestDto,
            BindingResult bindingResult) {
        likedClubService.createLike(likedClubRequestDto);

        return new ResponseEntity<>(new ResponseDto<>(1, "좋아요 신청 성공", null), HttpStatus.CREATED);
    }

    //좋아요 취소
    @DeleteMapping
    public ResponseEntity<?> deleteLikedClub(
            @RequestParam("clubId") Long clubId,
            @RequestParam("userId") Long userId) {
        likedClubService.deleteLike(clubId, userId);

        return new ResponseEntity<>(new ResponseDto<>(1, "좋아요 취소 성공", null), HttpStatus.OK);
    }

    // 사용자가 등록한 좋아요 모임 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserLikedClubs(
            @PathVariable("userId") Long userId,
            @PageableDefault(size = 4) Pageable pageable) {

        LikedClubSearchPageResDto res = likedClubService.getLikedClubList(userId, pageable);

        return new ResponseEntity<>(new ResponseDto<>(1, "좋아요한 클럽 리스트 조회 성공", res), HttpStatus.OK);
    }

    // 사용자가 등록한 좋아요한 독서모임 아이디 조회
    @GetMapping("/ids")
    public ResponseEntity<?> getUserLikedClubIds(
            @RequestParam("userId") Long userId) {

        LikedClubIdListResDto res = likedClubService.getLikedClubIdList(userId);

        return new ResponseEntity<>(new ResponseDto<>(1, "좋아요한 클럽 id 리스트 조회 성공", res), HttpStatus.OK);
    }

}
