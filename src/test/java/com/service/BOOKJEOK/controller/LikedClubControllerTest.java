package com.service.BOOKJEOK.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.BOOKJEOK.domain.LikedClub;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.repository.user.UserRepository;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import com.service.BOOKJEOK.repository.likedclub.LikedClubRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private LikedClubRepository likedClubRepository;
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

    @WithMockUser
    @Test
    public void getUserLikedClubs_Test() throws Exception {
        //given
        User me = newUser("mjh", "abc");
        User mePS = userRepository.save(me);
        User user1 = newUser("abc", "abc");
        User user1PS = userRepository.save(user1);
        Club club1 = newClub("myClub1", user1PS);
        Club club2 = newClub("myClub2", user1PS);
        Club club3 = newClub("myClub3", user1PS);
        Club club1PS = clubRepository.save(club1);
        Club club2PS = clubRepository.save(club2);
        Club club3PS = clubRepository.save(club3);
        LikedClub like1 = likedClubRepository.save(LikedClub.builder().club(club1PS).user(mePS).build());
        LikedClub like2 = likedClubRepository.save(LikedClub.builder().club(club2PS).user(mePS).build());
        LikedClub like3 = likedClubRepository.save(LikedClub.builder().club(club3PS).user(user1PS).build());

        //when
        ResultActions resultActions = mvc.perform(get("/likedclubs/users/" + mePS.getId())
                .param("page", "0"));
        //String res = resultActions.andReturn().getResponse().getContentAsString();
        //System.out.println("테스트 : " + res);

        //then
        resultActions.andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    public void getUserLikedClubIds_Test() throws Exception {
        //given
        User me = newUser("mjh", "abc");
        User mePS = userRepository.save(me);
        User user1 = newUser("abc", "abc");
        User user1PS = userRepository.save(user1);
        Club club1 = newClub("myClub1", user1PS);
        Club club2 = newClub("myClub2", user1PS);
        Club club3 = newClub("myClub3", user1PS);
        Club club1PS = clubRepository.save(club1);
        Club club2PS = clubRepository.save(club2);
        Club club3PS = clubRepository.save(club3);
        LikedClub like1 = likedClubRepository.save(LikedClub.builder().club(club1PS).user(mePS).build());
        LikedClub like2 = likedClubRepository.save(LikedClub.builder().club(club2PS).user(mePS).build());
        LikedClub like3 = likedClubRepository.save(LikedClub.builder().club(club3PS).user(user1PS).build());

        //when
        ResultActions resultActions = mvc.perform(get("/likedclubs/ids")
                .param("userId", mePS.getId().toString()));
        //String res = resultActions.andReturn().getResponse().getContentAsString();
        //System.out.println(res);

        //then
        resultActions.andExpect(status().isOk());
    }

}