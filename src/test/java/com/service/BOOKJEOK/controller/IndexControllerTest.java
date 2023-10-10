package com.service.BOOKJEOK.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.BOOKJEOK.domain.Comment;
import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import com.service.BOOKJEOK.repository.comment.CommentRepository;
import com.service.BOOKJEOK.repository.feed.FeedRepository;
import com.service.BOOKJEOK.repository.user.UserRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class IndexControllerTest extends DummyObject {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private CommentRepository commentRepository;
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
        commentRepository.save(comment1);
        Comment comment2 = Comment.builder()
                .contents("contents2")
                .user(mePS)
                .feed(feedPS)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        commentRepository.save(comment2);

        this.feedId = feedPS.getId();
        this.clubId = clubPS.getId();
        this.userId = mePS.getId();
    }

    @Test
    public void mainPageFeedList_Test() throws Exception {
        //given

        //when
        ResultActions resultActions = mvc.perform(get("/main/feeds")
                .param("sortBy", "likes"));
        String res = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + res);

        //then
        resultActions.andExpect(status().isOk());
    }


}