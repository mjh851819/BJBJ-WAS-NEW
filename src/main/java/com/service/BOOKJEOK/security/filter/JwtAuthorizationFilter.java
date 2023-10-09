package com.service.BOOKJEOK.security.filter;

import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.security.dto.PrincipalUserDetails;
import com.service.BOOKJEOK.security.jwt.JwtError;
import com.service.BOOKJEOK.security.jwt.JwtService;
import com.service.BOOKJEOK.security.jwt.JwtVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        /*log.info("1");
        if(isHeaderVerify(request, response)){
            //토큰이 존재하면 여기까지 온다.
            log.info("2");
            String token = request.getHeader(ACCESS_TOKEN).replace(JwtVO.TOKEN_PREFIX, "");
            UserDetails loginUser = JwtProcess.verify(token);
            log.info("Authorities : " + loginUser.getAuthorities().toString());
            log.info("3");
            //임시 세션 등록, UsernamePasswordAuthenticationToken의 첫번째 파라미터로는 UserDetail 아니면 username이 올 수 있다. 여기선 username이 null이기 때문에 UserDetail을 넣어줌.
            //크게 중요한 사항이 아니다 중요한것은 loginUser.getAuthorities()이다.
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
        */

        log.info("인가 / 권한 검증");

        if(isHeaderVerify(request, response)){
            try {
                // 올바르지 않은 헤더는 재로그인
                if (jwtService.isValidHeaderOrThrow(request)) {
                    String refreshToken = jwtService.extractRefreshToken(request);
                    String accessToken = jwtService.extractAccessToken(request);
                    log.info("Access : " + accessToken);
                    log.info("Refresh : " + refreshToken);

                    // 만료된 리프레쉬 토큰은 재로그인
                    if (jwtService.isNotExpiredToken(refreshToken)) {

                        User userByToken = jwtService.getUserByToken(refreshToken);

                        // 리프레쉬 토큰이 1일 이내 만료일 경우 새로 발급
                        if (jwtService.isExpiredInHourTokenOrThrow(refreshToken)) {
                            log.info("[REFRESH TOKEN] > 리프레쉬 토큰 재발급");
                            refreshToken = jwtService.updateRefreshTokenOfUser(refreshToken);
                            jwtService.setResponseOfRefreshToken(response, refreshToken);
                        }

                        // 액세스 토큰이 만료된 경우 새로 발급
                        if (jwtService.checkValidTokenOrThrow(accessToken) == JwtError.JWT_ACCESS_EXPIRED) {
                            log.info("[ACCESS TOKEN] > 액세스 토큰 재발급");
                            String reissuedAccessToken = jwtService.createAccessToken(userByToken);
                            jwtService.setResponseOfAccessToken(response, reissuedAccessToken);
                        }

                        PrincipalUserDetails principal = new PrincipalUserDetails(userByToken);
                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                principal, null, principal.getAuthorities()
                        );

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
                
            } catch (AuthenticationException jwtException) {
                log.error("=================================인가 에러=================================");
                log.error(jwtException.getMessage());
                log.error("=================================인가 에러=================================");
                request.setAttribute(JwtVO.EXCEPTION, jwtException.getMessage());
            } catch (Exception e) {
                log.error("=================================미정의 에러=================================");
                log.error(e.getMessage());
                log.error("=================================미정의 에러=================================");
                e.printStackTrace();
                request.setAttribute(JwtVO.EXCEPTION, e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }

        private boolean isHeaderVerify(HttpServletRequest request, HttpServletResponse response){
        String access = request.getHeader(JwtVO.ACCESS_TOKEN_HEADER);
        String refresh = request.getHeader(JwtVO.REFRESH_TOKEN_HEADER);
        if(access == null || !access.startsWith(JwtVO.TOKEN_PREFIX) || refresh == null || !refresh.startsWith(JwtVO.TOKEN_PREFIX)) {
            return false;
        }
        return true;
    }
}
