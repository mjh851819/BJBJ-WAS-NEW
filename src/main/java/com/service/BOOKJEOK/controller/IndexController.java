package com.service.BOOKJEOK.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.BOOKJEOK.dto.club.ClubRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class IndexController {

    private final ObjectMapper om;

    @GetMapping
    public String index(){
        return "index";
    }


    @GetMapping("/tokenTest")
    public String tokenTest(@RequestParam String Access_Token, @RequestParam String Refresh_Token) {
        return Access_Token + '\n' + Refresh_Token;
    }

    @GetMapping("/auth")
    public String auth(Authentication authentication){
        Object principal = authentication.getAuthorities();
        System.out.println(principal);

        return "Ok";
    }

    @GetMapping("/user")
    public String user(){
        return "Ok";
    }

    @GetMapping("/page")
    public String Page(ClubRequestDto.ClubSearchReqDto clubSearchReqDto,
                       @PageableDefault(size = 9) Pageable pageable) {
        log.info("dto : " + clubSearchReqDto.toString());
        log.info("page : " + pageable);
        return "ok";
    }

}
