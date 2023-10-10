package com.service.BOOKJEOK.security;

import com.service.BOOKJEOK.security.filter.JwtAuthorizationFilter;
import com.service.BOOKJEOK.security.jwt.JwtService;
import com.service.BOOKJEOK.security.jwt.JwtVO;
import com.service.BOOKJEOK.security.oauth.OAuth2SuccessHandler;
import com.service.BOOKJEOK.util.CustomResponseUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final JwtService jwtService;
    private final OAuth2UserService customOAuth2UserService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        log.debug("디버그 : BCryptPasswordEncoder 빈 등록됨");
        return new BCryptPasswordEncoder();
    }

    // JWT 필터 등록이 필요
    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        // 여기서 필터를 등록한다.
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            //builder.addFilter(new JwtAuthenticationFilter(authenticationManager));
            builder.addFilter(new JwtAuthorizationFilter(authenticationManager, jwtService));
        }
    }

    //JWT 서버를 만들 예정. Session을 사용 안함.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("디버그: filterChain 빈 등록됨");
        http.headers().frameOptions().disable(); //iframe을 허용 안함
        http.csrf().disable(); // disable안하면 postman 작동 안함
        http.cors().configurationSource(configurationSource()); //javascript 요청 허용, 아래 public 메서드로 설정 주입

        // 필터 적용
        http.apply(new CustomSecurityFilterManager());

        /*
         * 기존 SpringBoot에서 Security 관련 예외가 터지면 ExceptionTranslationFilter가 예외를 가로채서 Http나 json에 맞게 예외를 출력해준다.
         * 아래 메소드로 ExceptionTranslationFilter의 제어권을 빼앗아서 일관성있는 예외 출력을 구현한다.
         * */
        http.exceptionHandling().authenticationEntryPoint((request, response, oauthException) -> {
            if(request.getAttribute(JwtVO.EXCEPTION) != null) {
                CustomResponseUtil.fail(response, request.getAttribute(JwtVO.EXCEPTION).toString(), HttpStatus.UNAUTHORIZED);
            }
            else{
                CustomResponseUtil.fail(response, "로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
            }

        });
        // 권한 실패에 대한 핸들링
        http.exceptionHandling().accessDeniedHandler(((request, response, accessDeniedException) -> {
            CustomResponseUtil.fail(response, "접근 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }));



        //JSessionId를 서버에서 관리하지 않겠다.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.formLogin().disable();
        //브라우저가 팝업창을 이용해서 사용자 인증을 진행한다.
        http.httpBasic().disable();

        http.authorizeRequests()
                .antMatchers("/oauth2", "/login", "/tokenTest", "/main").permitAll()
                .anyRequest().authenticated();

        http.oauth2Login(oauth2 -> oauth2
                .successHandler(new OAuth2SuccessHandler(jwtService))
                .userInfoEndpoint()
                .userService(customOAuth2UserService));

        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        log.debug("디버그: configurationSource cors 설정이 SecurityFilterChain 에 등록됨");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE (javascript 요청 허용)
        configuration.addAllowedOriginPattern("*"); // 모든 ip 주소 허용 -> frontend쪽 ip만 허용할 수 있음
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
