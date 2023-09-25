package com.service.BOOKJEOK.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtVO {
    public static final String SECRET = "북적북적"; // HS256
    public static final int ACCESS_EXPIRATION_TIME = 1000 * 60 * 30;
    public static final int REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 24;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ACCESS_TOKEN = "Access_Token";
    public static final String REFRESH_TOKEN = "Refresh_Token";
    public static final String EXCEPTION = "EXCEPTION";
    public static final String EMAIL = "EMAIL";
    public static final String ACCESS_TOKEN_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "Authorization-refresh";


}
