package com.service.BOOKJEOK.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.domain.user.UserEnum;
import com.service.BOOKJEOK.dto.club.ClubRequestDto;
import com.service.BOOKJEOK.repository.user.UserRepository;
import com.service.BOOKJEOK.repository.club.ClubRepository;
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

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ClubControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private WebApplicationContext ctx;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp(){
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .alwaysDo(print())
                .build();

        User user = User.builder()
                .id(1L)
                .name("juhong")
                .email("abc@abc")
                .role(UserEnum.USER)
                .build();
        userRepository.save(user);

        for (int i = 0; i < 10; i++) {
            clubRepository.save(Club.builder()
                    .title("q1" + i)
                    .tags("소모임")
                    .build());
        }

    }

    @WithMockUser
    @Test
    public void createClub_success_Test() throws Exception {
        //given
        User userPS = userRepository.findByEmail("abc@abc").get();

        ClubRequestDto.ClubCreateReqDto clubCreateReqDto = ClubRequestDto.ClubCreateReqDto.builder()
                .userId(userPS.getId())
                .title("mjhClub")
                .img_url("123")
                .contents("abc")
                .max_personnel(3)
                .description("good")
                .tags("온라인")
                .bookTitle("qewr")
                .author("min")
                .publisher("abc")
                .build();
        String dto = om.writeValueAsString(clubCreateReqDto);

        //when
        ResultActions resultActions = mvc.perform(post("/clubs").content(dto).contentType(MediaType.APPLICATION_JSON));
        //String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        //System.out.println(responseBody);

        //then
        resultActions.andExpect(status().isCreated());
    }

    @WithMockUser
    @Test
    public void search_club_Test() throws Exception {
        //given

        //when
        ResultActions resultActions = mvc.perform(get("/clubs").param("sortBy","createdAt")
                //.param("keyword", "abc")
                //.param("tags", "ONLINE,SMALL")
                .param("page","0"));

        //String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        //System.out.println("테스트 : " + contentAsString);

        //then
        resultActions.andExpect(status().isOk());

    }

    @WithMockUser
    @Test
    public void getClubDetail_test() throws Exception {
        //given
        Long myClubId = 100L;
        User myUser = newMockUser(101L, "juhong", "mjh8518@naver.com");
        User myUserPS = userRepository.save(myUser);

        Club myClub = newMockClub(myClubId, "MyClub", myUserPS);
        Club myClubPS = clubRepository.save(myClub);

        //when
        ResultActions resultActions = mvc.perform(get("/clubs/" + myClubPS.getId()));
        //String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        //System.out.println(contentAsString);

        //then
        resultActions.andExpect(status().isOk());

    }

    @WithMockUser
    @Test
    public void getUserClub_test() throws Exception {
        //given
        Long myClubId = 100L;
        User myUser = newMockUser(101L, "juhong", "mjh8518@naver.com");
        User myUserPS = userRepository.save(myUser);

        Club myClub = newMockClub(myClubId, "MyClub", myUserPS);
        Club myClubPS = clubRepository.save(myClub);

        //when
        ResultActions resultActions = mvc.perform(get("/clubs/users/" + myUserPS.getId()));
        //String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        //System.out.println(contentAsString);

        //then
        resultActions.andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    public void updateClub() throws Exception {
        //given
        Long myClubId = 100L;
        User myUser = newMockUser(101L, "juhong", "mjh8518@naver.com");
        User myUserPS = userRepository.save(myUser);

        Club myClub = newMockClub(myClubId, "MyClub", myUserPS);
        Club myClubPS = clubRepository.save(myClub);
        ClubRequestDto.ClubUpdateReqDto req = ClubRequestDto.ClubUpdateReqDto.builder()
                .clubId(1L)
                .title("update")
                .img_url("update")
                .contents("update")
                .max_personnel(1)
                .description("update")
                .tags("소모임")
                .bookTitle("update")
                .author("update")
                .publisher("update")
                .build();
        String dto = om.writeValueAsString(req);

        em.flush();
        em.clear();

        //when
        ResultActions resultActions = mvc.perform(put("/clubs/users/" + myUserPS.getId()).content(dto).contentType(MediaType.APPLICATION_JSON));
        //String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        //System.out.println("테스트 : " + responseBody);

        //then
        resultActions.andExpect(status().isOk());
    }
    @WithMockUser
    @Test
    public void deleteClub_Test() throws Exception {
        //given
        Long myClubId = 100L;
        User myUser = newMockUser(101L, "juhong", "mjh8518@naver.com");
        User myUserPS = userRepository.save(myUser);

        Club myClub = newMockClub(myClubId, "MyClub", myUserPS);
        Club myClubPS = clubRepository.save(myClub);
        //when
        ResultActions resultActions = mvc.perform(delete("/clubs/users")
                .param("userId", myUserPS.getId().toString())
        );

        //then
        resultActions.andExpect(status().isOk());

    }
}