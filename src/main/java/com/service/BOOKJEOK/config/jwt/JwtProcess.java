package com.service.BOOKJEOK.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.service.BOOKJEOK.config.jwt.JwtVO.SECRET;

public class JwtProcess {

    // Access Token 생성
    public static String delegateAccessToken(String email) {
        String jwtToken = JWT.create()
                .withSubject("BJBJ")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.ACCESS_EXPIRATION_TIME))
                .withClaim("id", email)
                .sign(Algorithm.HMAC512(SECRET));

        return JwtVO.TOKEN_PREFIX+jwtToken;
    }

    // Refresh Token 생성
    public static String delegateRefreshToken(String email) {
        String jwtToken = JWT.create()
                .withSubject("bank")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.REFRESH_EXPIRATION_TIME))
                .withClaim("id", email)
                .sign(Algorithm.HMAC512(SECRET));

        return JwtVO.TOKEN_PREFIX+jwtToken;
    }
}
