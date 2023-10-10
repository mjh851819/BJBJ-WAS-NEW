package com.service.BOOKJEOK.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class IndexController {

    private final ObjectMapper om;
    private final ClubService clubService;

    @GetMapping
    public String index(){
        return "index";
    }


    @GetMapping("/tokenTest")
    public String tokenTest(@RequestParam String Access_Token, @RequestParam String Refresh_Token) {
        return Access_Token + '\n' + Refresh_Token;
    }

    @GetMapping("/main")
    public ResponseEntity<?> mainPageLikesClub(@RequestParam("sortBy") String sortBy) {
        ClubResponseDto.ClubSearchPageResDto res = clubService.searchClubForMain(sortBy);

        return new ResponseEntity<>(new ResponseDto<>(1, "독서모임 조회 성공", res), HttpStatus.OK);
    }

}
