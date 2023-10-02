package com.service.BOOKJEOK.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.BOOKJEOK.domain.LikedClub;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.dto.likedclub.LikedClubRequestDto;
import com.service.BOOKJEOK.dto.member.MemberRequestDto;
import com.service.BOOKJEOK.repository.UserRepository;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import com.service.BOOKJEOK.service.LikedClubService;
import com.service.BOOKJEOK.util.dummy.DummyObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static com.service.BOOKJEOK.dto.likedclub.LikedClubRequestDto.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class LikedClubControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private LikedClubService likedClubService;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private WebApplicationContext ctx;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .alwaysDo(print())
                .build();
    }

    @WithMockUser
    @Test
    public void createLikedClub_Test() throws Exception {
        //given
        User myUser = newUser("mjh", "abc@abc.com");
        User userPS = userRepository.save(myUser);
        User user = userRepository.save(newUser("def", "def@def.com"));
        Club club = newClub("myclub", user);
        Club clubPS = clubRepository.save(club);

        LikedClubCreateReqDto req = new LikedClubCreateReqDto(userPS.getId(), clubPS.getId());
        String dto = om.writeValueAsString(req);

        //when
        ResultActions resultActions = mvc.perform(post("/likedclubs").content(dto).contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isCreated());
    }

    @WithMockUser
    @Test
    public void deleteLikedClub_Test() throws Exception {
        //given
        User myUser = newUser("mjh", "abc@abc.com");
        User userPS = userRepository.save(myUser);
        User user = userRepository.save(newUser("def", "def@def.com"));
        Club club = newClub("myclub", user);
        Club clubPS = clubRepository.save(club);
        likedClubService.createLike(new LikedClubCreateReqDto(userPS.getId(), clubPS.getId()));


        //when
        ResultActions resultActions = mvc.perform(delete("/likedclubs")
                .param("userId", userPS.getId().toString())
                .param("clubId", clubPS.getId().toString()));

        //then
        resultActions.andExpect(status().isOk());
    }

}