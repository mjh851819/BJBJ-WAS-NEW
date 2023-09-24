package com.service.BOOKJEOK.config.Auth;

import com.service.BOOKJEOK.config.jwt.JwtProcess;
import com.service.BOOKJEOK.config.jwt.JwtVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.service.BOOKJEOK.config.jwt.JwtVO.ACCESS_TOKEN;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("1");
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
    }

    private boolean isHeaderVerify(HttpServletRequest request, HttpServletResponse response){
        String header = request.getHeader(ACCESS_TOKEN);
        if(header == null || !header.startsWith(JwtVO.TOKEN_PREFIX)) {
            return false;
        }
        return true;
    }
}
