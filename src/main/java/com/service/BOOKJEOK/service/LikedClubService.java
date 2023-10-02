package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.LikedClub;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.dto.likedclub.LikedClubRequestDto;
import com.service.BOOKJEOK.handler.ex.CustomApiException;
import com.service.BOOKJEOK.handler.ex.ExMessage;
import com.service.BOOKJEOK.repository.LikedClubRepository;
import com.service.BOOKJEOK.repository.UserRepository;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.service.BOOKJEOK.dto.likedclub.LikedClubRequestDto.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LikedClubService {

    private final LikedClubRepository likedClubRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;

    @Transactional
    public void createLike(LikedClubCreateReqDto req) {
        User userPS = userRepository.findById(req.getUserId()).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_USER));
        Club clubPS = clubRepository.findById(req.getClubId()).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_CLUB));

        if(likedClubRepository.findByUserAndClub(userPS, clubPS).isPresent()) {
            throw new CustomApiException(ExMessage.ALREADY_LIKED_CLUB);
        }

        LikedClub likedclub = LikedClub.builder()
                .club(clubPS)
                .user(userPS)
                .build();

        likedClubRepository.save(likedclub);
    }
}
