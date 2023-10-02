package com.service.BOOKJEOK.controller;

import com.service.BOOKJEOK.domain.member.Member;
import com.service.BOOKJEOK.dto.ResponseDto;
import com.service.BOOKJEOK.dto.member.MemberRequestDto;
import com.service.BOOKJEOK.dto.member.MemberResponseDto;
import com.service.BOOKJEOK.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.service.BOOKJEOK.dto.member.MemberRequestDto.*;
import static com.service.BOOKJEOK.dto.member.MemberResponseDto.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //참여신청
    @PostMapping
    public ResponseEntity<?> memberApply(
            @RequestBody MemberApplyReqDto memberApplyReqDto,
            BindingResult bindingResult) {
        memberService.apply(memberApplyReqDto);

        return new ResponseEntity<>(new ResponseDto<>(1, "클럽 가입신청 성공", null), HttpStatus.CREATED);
    }

    //참여신청 취소, 거절, 멤버 내보내기
    @DeleteMapping
    public ResponseEntity<?> memberDelete(
            @RequestParam("userId") Long userId,
            @RequestParam("clubId") Long clubId,
            @RequestParam("myId") Long myId) {
        memberService.delete(userId, clubId, myId);
        return new ResponseEntity<>(new ResponseDto<>(1, "멤버 삭제 성공", null), HttpStatus.OK);
    }

    //참여 승인
    @PutMapping
    public ResponseEntity<?> memberApprove(
            @RequestBody MemberApproveReqDto memberApproveReqDto,
            BindingResult bindingResult) {
        memberService.approve(memberApproveReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "참여 승인 성공", null), HttpStatus.OK);
    }

    //승인 대기자 목록 조회, 참여자 목록 조회 (approvalStatus: WAITING -> 승인대기자, CONFIRMED -> 참여자)
    @GetMapping
    public ResponseEntity<?> getMembers(
            @RequestParam("userId") Long userId,
            @RequestParam("approvalStatus") String approvalStatus,
            @PageableDefault(size = 4) Pageable pageable) {

        MemberSearchPageResDto res = memberService.getMemberList(userId, approvalStatus, pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "멤버 검색 성공", res), HttpStatus.OK);
    }

    //참여중인 독서모임 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getJoiningClubs(
            @PathVariable("userId") Long userId,
            @PageableDefault(size = 4) Pageable pageable) {

        MemberJoiningClubsPageResDto res = memberService.getJoiningClubList(userId, pageable);

        return new ResponseEntity<>(new ResponseDto<>(1, "독서모임 검색 성공", res), HttpStatus.OK);
    }


}
