package com.service.BOOKJEOK.controller;

import com.service.BOOKJEOK.domain.member.Member;
import com.service.BOOKJEOK.dto.ResponseDto;
import com.service.BOOKJEOK.dto.member.MemberRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.service.BOOKJEOK.dto.member.MemberRequestDto.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    //참여신청
    @PostMapping
    public ResponseEntity<?> memberApply(
            @RequestBody MemberApplyReqDto memberApplyReqDto) {
        return new ResponseEntity<>(new ResponseDto<>(1, "클럽 가입신청 성공", null), HttpStatus.CREATED);
    }


}
