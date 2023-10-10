package com.service.BOOKJEOK.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.repository.user.UserRepository;
import com.service.BOOKJEOK.security.ex.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private String SECRET_KEY = JwtVO.SECRET;

    private final UserRepository userRepository;

    //토큰 생성 메서드
    public String createAccessToken(User user) {

        return JWT.create()
                .withSubject(JwtVO.ACCESS_TOKEN)
                .withClaim(JwtVO.EMAIL, user.getEmail())
                .withClaim("auth", user.getRole().getRole())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.ACCESS_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }
    public String createRefreshToken() {
        return JWT.create()
                .withSubject(JwtVO.REFRESH_TOKEN)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.REFRESH_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    //Request의 헤더에서 Token을 추출하는 메서드
    public String extractRefreshToken(HttpServletRequest request) {
        return request
                .getHeader(JwtVO.REFRESH_TOKEN_HEADER)
                .replace(JwtVO.TOKEN_PREFIX, "");
    }
    public String extractAccessToken(HttpServletRequest request) {
        return request
                .getHeader(JwtVO.ACCESS_TOKEN_HEADER)
                .replace(JwtVO.TOKEN_PREFIX, "");
    }

    public boolean isNotExpiredToken(String token) {
        try {
            JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(token);
        } catch (TokenExpiredException expiredException) {
            throw new JwtException(JwtError.JWT_REFRESH_EXPIRED);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new JwtException(JwtError.JWT_REFRESH_EXPIRED);
        }
        return true;
    }

    public boolean isExpiredInHourTokenOrThrow(String token) {
        try {
            Date expiresAt = JWT.require(Algorithm.HMAC512(SECRET_KEY))
                    .build()
                    .verify(token)
                    .getExpiresAt();

            Date current = new Date(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(current);
            calendar.add(Calendar.HOUR, 1);

            Date afterHourFromToday = calendar.getTime();
            if (expiresAt.before(afterHourFromToday)) {
                return true;
            }
        } catch (TokenExpiredException e) {
            throw new JwtException(JwtError.JWT_REFRESH_EXPIRED);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new JwtException(JwtError.JWT_REFRESH_NOT_VALID);
        }
        return false;
    }

    public boolean isValidHeaderOrThrow(HttpServletRequest request) {

        String accessToken = request.getHeader(JwtVO.ACCESS_TOKEN_HEADER);
        String refreshToken = request.getHeader(JwtVO.REFRESH_TOKEN_HEADER);

        if (accessToken != null && refreshToken != null) {
            if (accessToken.startsWith(JwtVO.TOKEN_PREFIX) && refreshToken.startsWith(JwtVO.TOKEN_PREFIX)) {
                return true;
            }
        }

        throw new JwtException(JwtError.JWT_HEADER_NOT_VALID);
    }

    public JwtError checkValidTokenOrThrow(String token) {
        try {
            JWT.require(Algorithm.HMAC512(SECRET_KEY))
                    .build()
                    .verify(token);
            return JwtError.JWT_VALID_TOKEN;
        } catch (TokenExpiredException e) {
            return JwtError.JWT_ACCESS_EXPIRED;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new JwtException(JwtError.JWT_ACCESS_NOT_VALID);
        }
    }

    public User getUserByToken(String token) {
        return userRepository.findByRefresh(token)
                .orElseThrow(() -> new JwtException(JwtError.JWT_MEMBER_NOT_FOUND_TOKEN));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new JwtException(JwtError.JWT_MEMBER_NOT_FOUND_EMAIL));
    }

    @Transactional
    public void setRefreshTokenToUser(String email, String token) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new JwtException(JwtError.JWT_MEMBER_NOT_FOUND_TOKEN))
                .setRefreshToken(token);
    }

    @Transactional
    public String updateRefreshTokenOfUser(String token) {
        String reissuedRefreshToken = createRefreshToken();

        userRepository.findByRefresh(token)
                .orElseThrow(() -> new JwtException(JwtError.JWT_MEMBER_NOT_FOUND_TOKEN))
                .setRefreshToken(reissuedRefreshToken);

        return reissuedRefreshToken;
    }

    //추후 사용시 테스트 코드 추가 필요함
    @Transactional
    public void removeRefreshTokenOfUser(String token) {
        userRepository.findByRefresh(token)
                .orElseThrow(() -> new JwtException(JwtError.JWT_MEMBER_NOT_FOUND_TOKEN))
                .setRefreshToken(null);
    }

    public void setResponseOfAccessToken(HttpServletResponse response, String token) {
        response.addHeader(JwtVO.ACCESS_TOKEN_HEADER, JwtVO.TOKEN_PREFIX + token);
    }

    public void setResponseOfRefreshToken(HttpServletResponse response, String token) {
        response.addHeader(JwtVO.REFRESH_TOKEN_HEADER, JwtVO.TOKEN_PREFIX + token);
    }

    //추후 사용시 테스트 코드 추가 필요함
    public void setResponseMessage(boolean result, HttpServletResponse response, String message) throws IOException {
        JSONObject object = new JSONObject();
        response.setContentType("application/json;charset=UTF-8");
        if (result) {
            response.setStatus(HttpServletResponse.SC_OK);
            object.put("success", true);
            object.put("code", 1);
            object.put("message", message);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            object.put("success", false);
            object.put("code", -1);
            object.put("message", message);
        }
        response.getWriter().print(object);
    }
}
