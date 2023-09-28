package com.service.BOOKJEOK.controller;

import com.service.BOOKJEOK.dto.ResponseDto;
import com.service.BOOKJEOK.dto.club.ClubRequestDto;
import com.service.BOOKJEOK.dto.club.ClubResponseDto;
import com.service.BOOKJEOK.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.service.BOOKJEOK.dto.club.ClubRequestDto.*;
import static com.service.BOOKJEOK.dto.club.ClubResponseDto.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/clubs")
public class ClubController {

    private final ClubService clubService;

    @PostMapping
    public ResponseEntity<?> createClub(@RequestBody @Valid ClubCreateReqDto reqDto, BindingResult bindingResult) {
        ClubCreateResDto res = clubService.createClub(reqDto);

        return new ResponseEntity<>(new ResponseDto<>(1, "클럽 생성 성공", res), HttpStatus.CREATED);
    }
}
