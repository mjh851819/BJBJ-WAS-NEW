package com.service.BOOKJEOK.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.domain.user.UserEnum;
import com.service.BOOKJEOK.repository.user.UserRepository;
import com.service.BOOKJEOK.security.ex.JwtException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Date;
import java.util.Optional;

import static com.service.BOOKJEOK.security.jwt.JwtVO.ACCESS_TOKEN_HEADER;
import static com.service.BOOKJEOK.security.jwt.JwtVO.REFRESH_TOKEN_HEADER;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock // 가짜 객체를 주입하는 ano, 가짜이기 때문에 stub 설정이 필요함
    private UserRepository userRepository;


    @Test
    public void create_test() throws Exception {
        //given
        User user = User.builder().id(1L).role(UserEnum.USER).email("abc@naver.com").build();

        //when
        String accessToken = jwtService.createAccessToken(user);
        String refreshToken = jwtService.createRefreshToken();
        System.out.println("테스트: " + accessToken);
        System.out.println("테스트: " + refreshToken);


        //then
        JWT.require(Algorithm.HMAC512(JwtVO.SECRET)).build().verify(accessToken);
        JWT.require(Algorithm.HMAC512(JwtVO.SECRET)).build().verify(refreshToken);
    }

    @Test
    public void isNotExpiredToken_test() throws Exception {
        //given
        User user = User.builder().id(1L).role(UserEnum.USER).email("abc@naver.com").build();
        String accessToken = jwtService.createAccessToken(user);

        String exToken = JWT.create()
                .withSubject(JwtVO.ACCESS_TOKEN)
                .withClaim(JwtVO.EMAIL, user.getEmail())
                .withClaim("auth", user.getRole().getRole())
                .withExpiresAt(new Date(System.currentTimeMillis()))
                .sign(Algorithm.HMAC512(JwtVO.SECRET));

        //when
        //then
        assertTrue(jwtService.isNotExpiredToken(accessToken));
        Assertions.assertThrows(JwtException.class, () -> jwtService.isNotExpiredToken(exToken));
    }

    @Test
    public void isExpiredInHourTokenOrThrow_Test() throws Exception {
        //given
        User user = User.builder().id(1L).role(UserEnum.USER).email("abc@naver.com").build();

        //when
        String accessToken = jwtService.createAccessToken(user);
        String refreshToken = jwtService.createRefreshToken();

        //then
        assertTrue((jwtService.isExpiredInHourTokenOrThrow(accessToken)));
        assertTrue(!(jwtService.isExpiredInHourTokenOrThrow(refreshToken)));
    }

    @Test
    public void isValidHeaderOrThrow_test() throws Exception {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletRequest request1 = new MockHttpServletRequest();
        request.addHeader(ACCESS_TOKEN_HEADER, JwtVO.TOKEN_PREFIX);
        request.addHeader(JwtVO.REFRESH_TOKEN_HEADER, JwtVO.TOKEN_PREFIX);

        //when
        //then
        assertTrue(jwtService.isValidHeaderOrThrow(request));
        Assertions.assertThrows(JwtException.class, () -> jwtService.isValidHeaderOrThrow(request1));
    }

    @Test
    public void checkValidTokenOrThrow_test() throws Exception {
        //given
        User user = User.builder().id(1L).role(UserEnum.USER).email("abc@naver.com").build();
        String accessToken = jwtService.createAccessToken(user);

        String exToken = JWT.create()
                .withSubject(JwtVO.ACCESS_TOKEN)
                .withClaim(JwtVO.EMAIL, user.getEmail())
                .withClaim("auth", user.getRole().getRole())
                .withExpiresAt(new Date(System.currentTimeMillis()))
                .sign(Algorithm.HMAC512(JwtVO.SECRET));

        //when
        //then
        org.assertj.core.api.Assertions.assertThat(jwtService.checkValidTokenOrThrow(accessToken)).isEqualTo(JwtError.JWT_VALID_TOKEN);
        org.assertj.core.api.Assertions.assertThat(jwtService.checkValidTokenOrThrow(exToken)).isEqualTo(JwtError.JWT_ACCESS_EXPIRED);
        Assertions.assertThrows(JwtException.class, () -> jwtService.checkValidTokenOrThrow("request1"));

    }

    @Test
    public void getUserByEmail_Token_test() throws Exception {
        //given
        User user = User.builder().build();
        user.setRefreshToken("123");

        //when
        //then
        when(userRepository.findByRefresh(any())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        jwtService.getUserByEmail("abc@abc.com");
        jwtService.getUserByToken("abc@abc.com");
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.findByRefresh(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(JwtException.class, () -> jwtService.getUserByEmail("abc@abc.com"));
        Assertions.assertThrows(JwtException.class, () -> jwtService.getUserByToken("abc@abc.com"));
    }

    @Test
    public void setRefreshTokenToUser_test() throws Exception {
        //given
        User user = User.builder().build();

        //when
        //then
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        jwtService.setRefreshTokenToUser(user.getEmail(), "1234");

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(JwtException.class, () -> jwtService.setRefreshTokenToUser(user.getEmail(), "1234"));
    }

    @Test
    public void updateRefreshTokenOfUser_test() throws Exception {
        //given
        User user = User.builder().id(1L).role(UserEnum.USER).email("abc@naver.com").build();
        String refreshToken = jwtService.createRefreshToken();

        //when
        when(userRepository.findByRefresh(any())).thenReturn(Optional.of(user));
        String token = jwtService.updateRefreshTokenOfUser(refreshToken);

        //then
        assertTrue(jwtService.isNotExpiredToken(token));

    }

    @Test
    public void setResponseOfToken_test() throws Exception {
        //given
        User user = User.builder().id(1L).role(UserEnum.USER).email("abc@naver.com").build();
        String accessToken = jwtService.createAccessToken(user);
        String refreshToken = jwtService.createRefreshToken();
        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        jwtService.setResponseOfAccessToken(response,accessToken);
        jwtService.setResponseOfRefreshToken(response,refreshToken);

        //then
        org.assertj.core.api.Assertions.assertThat(response.getHeader(ACCESS_TOKEN_HEADER)).isEqualTo(JwtVO.TOKEN_PREFIX + accessToken);
        org.assertj.core.api.Assertions.assertThat(response.getHeader(REFRESH_TOKEN_HEADER)).isEqualTo(JwtVO.TOKEN_PREFIX + refreshToken);
    }



}