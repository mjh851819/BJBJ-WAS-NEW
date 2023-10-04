package com.service.BOOKJEOK.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.dto.feed.FeedRequestDto;
import com.service.BOOKJEOK.repository.UserRepository;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import com.service.BOOKJEOK.repository.feed.FeedRepository;
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

import static com.service.BOOKJEOK.dto.feed.FeedRequestDto.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class FeedControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private FeedRepository feedRepository;
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
    public void createFeed_Test() throws Exception {
        //given
        User me = newUser("mjh", "abc");
        User mePS = userRepository.save(me);
        Club club = newClub("club1", mePS);
        Club clubPS = clubRepository.save(club);
        FeedCreateReqDto req = new FeedCreateReqDto(mePS.getId(), clubPS.getId(), "title1", "contents1", "url");
        String dto = om.writeValueAsString(req);

        //when
        ResultActions resultActions = mvc.perform(post("/feeds").content(dto).contentType(MediaType.APPLICATION_JSON));
        //String res = resultActions.andReturn().getResponse().getContentAsString();
        //System.out.println("테스트 : " + res);

        //then
        resultActions.andExpect(status().isCreated());
    }

    @WithMockUser
    @Test
    public void updateFeed_Test() throws Exception {
        //given
        User me = newUser("mjh", "abc");
        User mePS = userRepository.save(me);
        Club club = newClub("club1", mePS);
        Club clubPS = clubRepository.save(club);
        Feed feed = newFeed("myFeed", mePS, clubPS);
        Feed feedPS = feedRepository.save(feed);
        FeedUpdateReqDto req = new FeedUpdateReqDto(feedPS.getId(), mePS.getId(), clubPS.getId(), "abc", "abc", "abc");
        String dto = om.writeValueAsString(req);

        //when
        ResultActions resultActions = mvc.perform(put("/feeds").content(dto).contentType(MediaType.APPLICATION_JSON));
        //String res = resultActions.andReturn().getResponse().getContentAsString();
        //System.out.println("테스트 : " + res);

        //then
        resultActions.andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    public void deleteFeed_Test() throws Exception {
        //given
        User me = newUser("mjh", "abc");
        User mePS = userRepository.save(me);
        Club myClub = newClub("club", mePS);
        Club clubPS = clubRepository.save(myClub);
        Feed feed = newFeed("feed", mePS, clubPS);
        Feed feedPS = feedRepository.save(feed);

        //when
        ResultActions resultActions = mvc.perform(delete("/feeds")
                        .param("feedId", feedPS.getId().toString()));
        //String res = resultActions.andReturn().getResponse().getContentAsString();
        //System.out.println("테스트 : " + res);

        //then
        resultActions.andExpect(status().isOk());
    }
}