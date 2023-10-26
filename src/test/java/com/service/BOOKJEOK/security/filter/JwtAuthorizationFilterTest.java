package com.service.BOOKJEOK.security.filter;

import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.domain.user.UserEnum;
import com.service.BOOKJEOK.repository.user.UserRepository;
import com.service.BOOKJEOK.security.jwt.JwtService;
import com.service.BOOKJEOK.security.jwt.JwtVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtAuthorizationFilterTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void authorization_success_test() throws Exception {
        //given
        User user = User.builder().role(UserEnum.USER).email("abc@abc.com").img_url("123").name("mjh").provider("google").providerId("sub").build();
        String ac = JwtVO.TOKEN_PREFIX + jwtService.createAccessToken(user);
        String rf = jwtService.createRefreshToken(user);
        user.setRefreshToken(rf);
        userRepository.save(user);
        rf = JwtVO.TOKEN_PREFIX + rf;

        //when
        ResultActions resultActions = mvc.perform(get("/").header(JwtVO.ACCESS_TOKEN_HEADER, ac).header(JwtVO.REFRESH_TOKEN_HEADER, rf));

        //then
        resultActions.andExpect(status().isOk());
    }


    @Test
    public void authorization_fail_test() throws Exception {
        //given

        //when
        ResultActions resultActions = mvc.perform(get("/"));
        //then
        resultActions.andExpect(status().isUnauthorized());
    }

}