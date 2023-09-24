package com.service.BOOKJEOK.config.jwt;

public class JwtVO {
    public static final String SECRET = "북적북적"; // HS256
    public static final int ACCESS_EXPIRATION_TIME = 1000 * 60 * 30;
    public static final int REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 24;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";
}
