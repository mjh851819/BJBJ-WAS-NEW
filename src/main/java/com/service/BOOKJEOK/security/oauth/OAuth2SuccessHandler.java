package com.service.BOOKJEOK.security.oauth;

import com.service.BOOKJEOK.domain.User;
import com.service.BOOKJEOK.security.dto.CustomOauth2UserDetails;
import com.service.BOOKJEOK.security.jwt.JwtService;
import com.service.BOOKJEOK.security.jwt.JwtVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOauth2UserDetails oAuth2User = (CustomOauth2UserDetails) authentication.getPrincipal();

        redirect(request, response, oAuth2User);  // Access Token과 Refresh Token을 Frontend에 전달하기 위해 Redirect
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, CustomOauth2UserDetails oAuth2User) throws IOException {
        //log.info("Token 생성 시작");
        String email = oAuth2User.getUser().getEmail();

        String accessToken = jwtService.createAccessToken(oAuth2User.getUser());  // Access Token 생성
        String refreshToken = jwtService.createRefreshToken();     // Refresh Token 생성
        //인증 성공 로직이기 때문에 반드시 객체가 존재한다.
        User user = jwtService.getUserByEmail(email);
        jwtService.setRefreshTokenToUser(user, refreshToken);

        String uri = createURI(accessToken, refreshToken).toString();   // Access Token과 Refresh Token을 포함한 URL을 생성
        getRedirectStrategy().sendRedirect(request, response, uri);   // sendRedirect() 메서드를 이용해 Frontend 애플리케이션 쪽으로 리다이렉트
    }

    // Redirect URI 생성. JWT를 쿼리 파라미터로 담아 전달한다.
    private URI createURI(String accessToken, String refreshToken) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add(JwtVO.ACCESS_TOKEN,JwtVO.TOKEN_PREFIX +  accessToken);
        queryParams.add(JwtVO.REFRESH_TOKEN,JwtVO.TOKEN_PREFIX +  refreshToken);

        return UriComponentsBuilder
                .newInstance()
                .fromUriString("http://localhost:8080")
                .path("/tokenTest")
                .queryParams(queryParams)
                .build()
                .toUri();
    }
}
