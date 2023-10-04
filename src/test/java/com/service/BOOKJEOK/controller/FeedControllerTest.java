package com.service.BOOKJEOK.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.BOOKJEOK.domain.Comment;
import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.dto.feed.FeedRequestDto;
import com.service.BOOKJEOK.repository.UserRepository;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import com.service.BOOKJEOK.repository.feed.FeedRepository;
import com.service.BOOKJEOK.util.PathMessage;
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

import java.time.LocalDateTime;

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

    private Long feedId;
    private Long clubId;
    private Long userId;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .alwaysDo(print())
                .build();
        User me = newUser("mjh", "abc");
        User mePS = userRepository.save(me);
        Club myClub = newClub("club", mePS);
        Club clubPS = clubRepository.save(myClub);
        Feed feed = newFeed("feed", mePS, clubPS);
        Feed feedPS = feedRepository.save(feed);
        Comment comment1 = Comment.builder()
                .contents("contents1")
                .user(mePS)
                .feed(feedPS)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Comment comment2 = Comment.builder()
                .contents("contents2")
                .user(mePS)
                .feed(feedPS)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        this.feedId = feedPS.getId();
        this.clubId = clubPS.getId();
        this.userId = mePS.getId();
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
        FeedUpdateReqDto req = new FeedUpdateReqDto(feedId, userId, clubId, "abc", "abc", "abc");
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
        //when
        ResultActions resultActions = mvc.perform(delete("/feeds")
                        .param("feedId", feedId.toString()));
        //String res = resultActions.andReturn().getResponse().getContentAsString();
        //System.out.println("테스트 : " + res);

        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void getFeedDetail_Test() throws Exception {
        //given
        Long feedId = this.feedId;

        //when
        ResultActions resultActions = mvc.perform(get("/feeds/"+feedId));
        //String res = resultActions.andReturn().getResponse().getContentAsString();
        //System.out.println("테스트 : " + res);

        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void getClubFeedList_Test() throws Exception {
        //given
        //when
        ResultActions resultActions = mvc.perform(get("/feeds/clubs/"+clubId));
        //String res = resultActions.andReturn().getResponse().getContentAsString();
        //System.out.println("테스트 : " + res);

        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void getFeedList_Test() throws Exception {
        //given
        //when
        ResultActions resultActions = mvc.perform(get("/feeds")
                .param("sortBy", PathMessage.CREATED_AT)
                .param("page", "1"));
        String res = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + res);

        //then
        resultActions.andExpect(status().isOk());
    }
}