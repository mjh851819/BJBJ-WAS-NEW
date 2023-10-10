package com.service.BOOKJEOK.controller;

import com.service.BOOKJEOK.dto.ResponseDto;
import com.service.BOOKJEOK.security.dto.CustomOauth2UserDetails;
import com.service.BOOKJEOK.security.dto.PrincipalUserDetails;
import com.service.BOOKJEOK.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.service.BOOKJEOK.dto.user.UserResponseDto.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    //유저 정보 상세 조회
    @GetMapping
    public ResponseEntity<?> searchUser(Authentication authentication) {
        PrincipalUserDetails principal = (PrincipalUserDetails) authentication.getPrincipal();
        UserSearchResDto userSearchResDto = new UserSearchResDto(principal.getUser());

        return new ResponseEntity<>(new ResponseDto<>(1, "유저 검색 성공", userSearchResDto), HttpStatus.OK);
    }

}
