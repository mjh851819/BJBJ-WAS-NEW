package com.service.BOOKJEOK.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.service.BOOKJEOK.config.oauth.CustomOAuth2User;
import com.service.BOOKJEOK.domain.UserEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

import static com.service.BOOKJEOK.config.jwt.JwtVO.SECRET;

public class JwtProcess {

    // Access Token 생성
    public static String delegateAccessToken(String email, Collection<? extends GrantedAuthority> roles) {
        String authorities = roles.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String jwtToken = JWT.create()
                .withSubject("BJBJ")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.ACCESS_EXPIRATION_TIME))
                .withClaim("id", email)
                .withClaim("auth", authorities)
                .sign(Algorithm.HMAC512(SECRET));

        return JwtVO.TOKEN_PREFIX+jwtToken;
    }

    // Refresh Token 생성
    public static String delegateRefreshToken(String email, Collection<? extends GrantedAuthority> roles) {
        String authorities = roles.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String jwtToken = JWT.create()
                .withSubject("bank")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.REFRESH_EXPIRATION_TIME))
                .withClaim("id", email)
                .sign(Algorithm.HMAC512(SECRET));

        return JwtVO.TOKEN_PREFIX+jwtToken;
    }

    //토큰 검증
    public static UserDetails verify(String token){
        //검증 실패시 이부분에서 예외가 터진다.
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET)).build().verify(token);

        //아래는 검증 성공 로직
        if (decodedJWT.getClaim("auth") == null) {
            //TODO:: Change Custom Exception
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        String role = decodedJWT.getClaim("auth").asString();
        //클레임에서 권한 정보 가져오기
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> role);

        UserDetails principal = new User(decodedJWT.getSubject(), "", authorities);
        return principal;
    }
}
