package com.service.BOOKJEOK.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.member.ApprovalStatus;
import com.service.BOOKJEOK.domain.member.Member;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.repository.member.MemberRepository;
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

import static com.service.BOOKJEOK.dto.member.MemberRequestDto.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class MemberControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private MemberRepository memberRepository;
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
    public void memberApply_Test() throws Exception {
        //given
        User myUser = newUser("mjh", "abc@abc.com");
        User userPS = userRepository.save(myUser);
        User user = userRepository.save(newUser("def", "def@def.com"));
        Club club = newClub("myclub", user);
        Club clubPS = clubRepository.save(club);

        MemberApplyReqDto req = new MemberApplyReqDto(userPS.getId(), clubPS.getId());
        String dto = om.writeValueAsString(req);

        //when
        ResultActions resultActions = mvc.perform(post("/members").content(dto).contentType(MediaType.APPLICATION_JSON));
        //String res = resultActions.andReturn().getResponse().getContentAsString();
        //System.out.println("테스트 : " + res);

        //then
        resultActions.andExpect(status().isCreated());
    }

    @WithMockUser
    @Test
    public void memberDelete_Test() throws Exception {
        //given
        User myUser = newUser("mjh", "abc@abc.com");
        User userPS = userRepository.save(myUser);
        User user = userRepository.save(newUser("def", "def@def.com"));
        Club club = newClub("myclub", user);
        Club clubPS = clubRepository.save(club);
        Member member = Member.builder()
                .club(clubPS)
                .user(userPS)
                .build();
        Member memberPS = memberRepository.save(member);


        //when
        ResultActions resultActions = mvc.perform(delete("/members")
                .param("userId", userPS.getId().toString())
                .param("clubId", clubPS.getId().toString()));
        //String res = resultActions.andReturn().getResponse().getContentAsString();
        //System.out.println("테스트 : " + res);

        //then
        resultActions.andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    public void memberApprove_Test() throws Exception {
        //given
        User myUser = newUser("mjh", "abc@abc.com");
        User userPS = userRepository.save(myUser);
        User user = userRepository.save(newUser("def", "def@def.com"));
        Club club = newClub("myclub", user);
        Club clubPS = clubRepository.save(club);
        Member member = Member.builder()
                .club(clubPS)
                .user(userPS)
                .build();
        Member memberPS = memberRepository.save(member);

        MemberApproveReqDto req = new MemberApproveReqDto(memberPS.getId());
        String dto = om.writeValueAsString(req);

        //when
        ResultActions resultActions = mvc.perform(put("/members").content(dto).contentType(MediaType.APPLICATION_JSON));
        //String res = resultActions.andReturn().getResponse().getContentAsString();
        //String message = resultActions.andReturn().getResolvedException().getMessage();
        //System.out.println("테스트 : " + res);

        //then
        resultActions.andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    public void getMembers_Test() throws Exception {
        //given
        User me = newMockUser(1L, "mjh", "abc@abc.com");
        User user1 = newMockUser(2L, "qwe", "abc@abc.com");
        User mePS = userRepository.save(me);
        User user1PS = userRepository.save(user1);

        Club club1 = newMockClub(1L, "abc", user1PS);
        Club club1PS = clubRepository.save(club1);

        Member member1 = Member.builder().club(club1PS).user(mePS).build();
        Member member2 = Member.builder().club(club1PS).user(mePS).build();
        Member member3 = Member.builder().club(club1PS).user(user1PS).build();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        //when
        ResultActions resultActions = mvc.perform(get("/members")
                .param("userId", user1PS.getId().toString())
                .param("approvalStatus", ApprovalStatus.WAITING.getValue())
                .param("page", "0"));

        //String res = resultActions.andReturn().getResponse().getContentAsString();
        //System.out.println("테스트 : " + res);

        //then
        resultActions.andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    public void getJoiningClubs_Test() throws Exception {
        //given
        User me = newMockUser(1L, "mjh", "abc@abc.com");
        User user1 = newMockUser(2L, "qwe", "abc@abc.com");
        User mePS = userRepository.save(me);
        User user1PS = userRepository.save(user1);

        Club club1 = newMockClub(1L, "abc", user1PS);
        Club club2 = newMockClub(2L, "cde", user1PS);
        Club club3 = newMockClub(3L, "fgh", user1PS);
        Club club1PS = clubRepository.save(club1);
        Club club2PS = clubRepository.save(club2);
        Club club3PS = clubRepository.save(club3);

        Member member1 = Member.builder().club(club1PS).user(mePS).build();
        member1.updateState();
        Member member2 = Member.builder().club(club2PS).user(mePS).build();
        member2.updateState();
        Member member3 = Member.builder().club(club3PS).user(user1PS).build();
        member3.updateState();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        //when
        ResultActions resultActions = mvc.perform(get("/members/users/" + mePS.getId())
                .param("page", "0"));

        //String res = resultActions.andReturn().getResponse().getContentAsString();
        //System.out.println("테스트 : " + res);

        //then
        resultActions.andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    public void getJoiningClubIds_Test() throws Exception {
        //given
        User me = newMockUser(1L, "mjh", "abc@abc.com");
        User user1 = newMockUser(2L, "qwe", "abc@abc.com");
        User mePS = userRepository.save(me);
        User user1PS = userRepository.save(user1);

        Club club1 = newMockClub(1L, "abc", user1PS);
        Club club2 = newMockClub(2L, "cde", user1PS);
        Club club3 = newMockClub(3L, "fgh", user1PS);
        Club club1PS = clubRepository.save(club1);
        Club club2PS = clubRepository.save(club2);
        Club club3PS = clubRepository.save(club3);

        Member member1 = Member.builder().club(club1PS).user(mePS).build();
        member1.updateState();
        Member member2 = Member.builder().club(club2PS).user(mePS).build();
        Member member3 = Member.builder().club(club3PS).user(user1PS).build();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        //when
        ResultActions resultActions = mvc.perform(get("/members/ids/")
                        .param("userId", mePS.getId().toString())
        );
        String res = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + res);

        //then
        resultActions.andExpect(status().isOk());
    }

}