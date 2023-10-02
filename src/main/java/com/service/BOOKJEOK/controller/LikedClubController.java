package com.service.BOOKJEOK.controller;

import com.service.BOOKJEOK.dto.ResponseDto;
import com.service.BOOKJEOK.dto.likedclub.LikedClubRequestDto;
import com.service.BOOKJEOK.service.LikedClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.service.BOOKJEOK.dto.likedclub.LikedClubRequestDto.*;

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

}
