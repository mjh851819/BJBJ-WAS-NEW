package com.service.BOOKJEOK.controller;

import com.service.BOOKJEOK.domain.member.Member;
import com.service.BOOKJEOK.dto.ResponseDto;
import com.service.BOOKJEOK.dto.member.MemberRequestDto;
import com.service.BOOKJEOK.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.service.BOOKJEOK.dto.member.MemberRequestDto.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //참여신청
    @PostMapping
    public ResponseEntity<?> memberApply(
            @RequestBody MemberApplyReqDto memberApplyReqDto) {
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


}
