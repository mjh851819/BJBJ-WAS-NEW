package com.service.BOOKJEOK.controller;

import com.service.BOOKJEOK.dto.ResponseDto;
import com.service.BOOKJEOK.dto.club.ClubRequestDto;
import com.service.BOOKJEOK.dto.club.ClubResponseDto;
import com.service.BOOKJEOK.service.ClubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.service.BOOKJEOK.dto.club.ClubRequestDto.*;
import static com.service.BOOKJEOK.dto.club.ClubResponseDto.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/clubs")
public class ClubController {

    private final ClubService clubService;

    //독서모임 생성
    @PostMapping
    public ResponseEntity<?> createClub(@RequestBody @Valid ClubCreateReqDto reqDto, BindingResult bindingResult) {
        ClubCreateResDto res = clubService.createClub(reqDto);

        return new ResponseEntity<>(new ResponseDto<>(1, "클럽 생성 성공", res), HttpStatus.CREATED);
    }

    //독서모임 리스트 조회
    @GetMapping
    public ResponseEntity<?> getClubList(
            ClubSearchReqDto clubSearchReqDto,
            @PageableDefault(size = 9) Pageable pageable) {

        ClubSearchPageResDto res = clubService.searchClub(clubSearchReqDto, pageable);

        return new ResponseEntity<>(new ResponseDto<>(1, "클럽 검색 성공", res), HttpStatus.OK);
    }

    //독서모임 상세조회
    @GetMapping("/{clubId}")
    public ResponseEntity<?> getClubDetail(
            @PathVariable Long clubId) {

        ClubSearchDetailResDto res = clubService.findClubById(clubId);
        return new ResponseEntity<>(new ResponseDto<>(1, "클럽 상세검색 성공", res), HttpStatus.OK);
    }

    //사용자가 만든 독서모임 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserClub(
            @PathVariable Long userId) {
        ClubSearchDetailResDto res = clubService.findClubByUserId(userId);
        return new ResponseEntity<>(new ResponseDto<>(1, "클럽 상세검색 성공", res), HttpStatus.OK);
    }

    // 독서모임 수정 (my page)
    @PutMapping("/users/{userId}")
    public ResponseEntity<?> updateClub(
            @RequestBody @Valid ClubUpdateReqDto clubUpdateReqDto,
            BindingResult bindingResult,
            @PathVariable Long userId) {

        log.debug("확인 : " + clubUpdateReqDto);

        clubService.updateClub(clubUpdateReqDto, userId);

        return new ResponseEntity<>(new ResponseDto<>(1, "클럽 업데이트 성공", null), HttpStatus.OK);
    }
}
