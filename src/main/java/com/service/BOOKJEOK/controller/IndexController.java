package com.service.BOOKJEOK.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.BOOKJEOK.dto.ResponseDto;
import com.service.BOOKJEOK.dto.club.ClubRequestDto;
import com.service.BOOKJEOK.dto.club.ClubResponseDto;
import com.service.BOOKJEOK.dto.feed.FeedResponseDto;
import com.service.BOOKJEOK.service.ClubService;
import com.service.BOOKJEOK.service.FeedService;
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

import static com.service.BOOKJEOK.dto.club.ClubResponseDto.*;
import static com.service.BOOKJEOK.dto.feed.FeedResponseDto.*;

@RequiredArgsConstructor
@RestController
public class IndexController {
    private final ClubService clubService;
    private final FeedService feedService;

    @GetMapping("/healthCheck")
    public String index(){
        return "index";
    }

    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/tokenTest")
    public String tokenTest(@RequestParam String Access_Token, @RequestParam String Refresh_Token) {
        return Access_Token + '\n' + Refresh_Token;
    }

    @GetMapping("/main/clubs")
    public ResponseEntity<?> mainPageClubList(@RequestParam("sortBy") String sortBy) {
        System.out.println("clubs 시작");
        ClubSearchPageResDto res = clubService.searchClubForMain(sortBy);
        System.out.println(res + "clubs 종료");

        return new ResponseEntity<>(new ResponseDto<>(1, "독서모임 조회 성공", res), HttpStatus.OK);
    }

    @GetMapping("/main/feeds")
    public ResponseEntity<?> mainPageFeedList(@RequestParam("sortBy") String sortBy) {
        System.out.println("feeds 시작");
        FeedSearchPageResDto res = feedService.searchFeedForMain(sortBy);
        System.out.println(res + "feeds 종료");

        return new ResponseEntity<>(new ResponseDto<>(1, "피드 조회 성공", res), HttpStatus.OK);
    }

}
