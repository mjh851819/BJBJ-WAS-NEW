package com.service.BOOKJEOK.repository.likedclub;

import com.service.BOOKJEOK.domain.LikedClub;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.dto.likedclub.LikedClubRequestDto;
import com.service.BOOKJEOK.dto.likedclub.LikedClubResponseDto;
import com.service.BOOKJEOK.repository.UserRepository;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import com.service.BOOKJEOK.util.dummy.DummyObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static com.service.BOOKJEOK.dto.likedclub.LikedClubResponseDto.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LikedClubRepositoryTest extends DummyObject {
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LikedClubRepository likedClubRepository;

    @Test
    public void searchClubList_Test() throws Exception {
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

        PageRequest pageRequest = PageRequest.of(0, 4);

        //when
        Page<LikedClubSearchResDto> res = likedClubRepository.searchClubList(mePS.getId(), pageRequest);

        //then
        Assertions.assertThat(res.getTotalElements()).isEqualTo(2);
    }

    @Test
    public void searchClubIdList() throws Exception {
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
        List<LikedClubIdResDto> res = likedClubRepository.searchClubIdList(mePS.getId());

        //then
        Assertions.assertThat(res.size()).isEqualTo(2);
    }

    @Test
    public void delete_Test() throws Exception {
        //given
        User myUser = newUser("mjh", "abc@abc.com");
        User userPS = userRepository.save(myUser);
        User user = userRepository.save(newUser("def", "def@def.com"));
        Club club = newClub("myclub", user);
        Club clubPS = clubRepository.save(club);
        likedClubRepository.save(LikedClub.builder().club(clubPS).user(userPS).build());
        LikedClub likedClub = likedClubRepository.findByUserAndClub(userPS, clubPS).get();


        //when
        likedClubRepository.delete(likedClub);
        //then
        Assertions.assertThat(likedClubRepository.findByUserAndClub(userPS, clubPS).isEmpty()).isTrue();
    }

}