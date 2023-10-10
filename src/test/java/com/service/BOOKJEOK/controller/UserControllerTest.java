package com.service.BOOKJEOK.controller;

import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.domain.user.UserEnum;
import com.service.BOOKJEOK.repository.user.UserRepository;
import com.service.BOOKJEOK.security.dto.PrincipalUserDetails;
import com.service.BOOKJEOK.util.dummy.DummyObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class UserControllerTest extends DummyObject {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WebApplicationContext ctx;

    private static final String name = "주홍";
    private static final String email = "mjh8518@naver.com";

    @BeforeEach
    public void setUp(){

        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .apply(springSecurity()) // 중요 : 시큐리티 설정을 위해서 등록해야 한다.
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .alwaysDo(print())
                .build();

        User user = User.builder()
                .id(1L)
                .name(name)
                .email(email)
                .role(UserEnum.USER)
                .build();
        User userPS = userRepository.save(user);

        PrincipalUserDetails principal = new PrincipalUserDetails(userPS);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void searchUser_Test() throws Exception {
        //given

        //when
        ResultActions resultActions = mvc.perform(get("/users"));
        //String responseBody = resultActions.andReturn().getResponse().getContentAsString();

        //then
        resultActions.andExpect(status().isOk());

    }

}