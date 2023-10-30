package com.service.BOOKJEOK.repository.club;

import com.service.BOOKJEOK.domain.Comment;
import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.member.Member;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.dto.club.ClubResponseDto;
import com.service.BOOKJEOK.repository.user.UserRepository;
import com.service.BOOKJEOK.repository.comment.CommentRepository;
import com.service.BOOKJEOK.repository.feed.FeedRepository;
import com.service.BOOKJEOK.repository.member.MemberRepository;
import com.service.BOOKJEOK.util.dummy.DummyObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static com.service.BOOKJEOK.dto.club.ClubRequestDto.*;
import static com.service.BOOKJEOK.dto.club.ClubResponseDto.*;

@DataJpaTest
class ClubRepositoryTest extends DummyObject {

    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EntityManager em;

    private Long userId;
    private Long clubId;
    private Long feedId;


    @BeforeEach
    public void Setup() {
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
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        Comment comment2 = Comment.builder()
                .contents("contents2")
                .user(mePS)
                .feed(feedPS)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        Member member1 = Member.builder().club(clubPS).user(mePS).build();
        member1.updateState();
        Member member2 = Member.builder().club(clubPS).user(mePS).build();
        Member member3 = Member.builder().club(clubPS).user(mePS).build();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        this.clubId = clubPS.getId();
        this.userId = mePS.getId();
        this.feedId = feedPS.getId();
    }

    @Test
    public void searchClub_test() throws Exception {
        //given
        ClubSearchReqDto clubSearchReqDto = ClubSearchReqDto.builder()
                //.keyword("")
                //.tags("")
                .sortBy("createdAt")
                .build();

        PageRequest pageRequest = PageRequest.of(1, 2);

        //when
        Page<Club> clubs = clubRepository.searchClub(clubSearchReqDto, pageRequest);


        //then
        Assertions.assertThat(clubs.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(clubs.getTotalElements()).isEqualTo(1);
    }

    @Test
    public void findByUserId_Test() {
        //given

        //when
        Club res = clubRepository.findByUserId(userId).get();
        //System.out.println("테스트 : " + res.getTitle());

        //then
        Assertions.assertThat(res.getTitle()).isEqualTo("club");
        Assertions.assertThat(res.getUser().getName()).isEqualTo("mjh");
    }

    @Test
    public void clubUpdate_Test() throws Exception {
        //given
        ClubUpdateReqDto req = ClubUpdateReqDto.builder()
                .clubId(clubId)
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

        //when
        em.flush();
        em.clear();
        Club findClub = clubRepository.findByUserId(userId).get();
        findClub.updateClub(req);
        em.flush();
        em.clear();
        Club res = clubRepository.findByUserId(userId).get();
        //System.out.println("테스트 : " + res.getTags().get(0).getTag().getValue());

        //then
        Assertions.assertThat(res.getTitle()).isEqualTo("update");
    }

    @Test
    public void deleteClub_Test() throws Exception {
        //given

        //when
        Club club = clubRepository.findById(clubId).get();
        List<Member> memberBefore = memberRepository.findAll();
        List<Comment> commentBefore = commentRepository.findAll();
        List<Feed> feedBefore = feedRepository.findAll();
        Assertions.assertThat(memberBefore.size()).isEqualTo(3);
        Assertions.assertThat(commentBefore.size()).isEqualTo(2);
        Assertions.assertThat(feedBefore.size()).isEqualTo(1);

        clubRepository.deleteClub(club);
        List<Member> afterMember = memberRepository.findAll();
        List<Comment> afterComment = commentRepository.findAll();
        List<Feed> afterFeed = feedRepository.findAll();

        //then
        Assertions.assertThat(afterMember.size()).isEqualTo(0);
        //Assertions.assertThat(afterComment.size()).isEqualTo(0);
        //Assertions.assertThat(afterFeed.size()).isEqualTo(0);
    }

    @Test
    public void find4ClubList_Test() throws Exception {
        //given

        //when
        List<ClubSearchResDto> res = clubRepository.find4ClubList("likes");
        //then
        Assertions.assertThat(res.size()).isEqualTo(1);

    }

}