package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.LikedClub;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.handler.ex.CustomApiException;
import com.service.BOOKJEOK.handler.ex.ExMessage;
import com.service.BOOKJEOK.repository.likedclub.LikedClubRepository;
import com.service.BOOKJEOK.repository.user.UserRepository;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.service.BOOKJEOK.dto.likedclub.LikedClubRequestDto.*;
import static com.service.BOOKJEOK.dto.likedclub.LikedClubResponseDto.*;

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

        clubPS.createLike();

        LikedClub likedclub = LikedClub.builder()
                .club(clubPS)
                .user(userPS)
                .build();

        likedClubRepository.save(likedclub);
    }

    @Transactional
    public void deleteLike(Long clubId, Long userId) {
        User userPS = userRepository.findById(userId).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_USER));
        Club clubPS = clubRepository.findById(clubId).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_CLUB));
        LikedClub likedClubPS = likedClubRepository.findByUserAndClub(userPS, clubPS).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_LIKE));

        clubPS.deleteLike();

        likedClubRepository.delete(likedClubPS);
    }

    public LikedClubSearchPageResDto getLikedClubList(Long userId, Pageable pageable) {

        Page<LikedClubSearchResDto> res = likedClubRepository.searchClubList(userId, pageable);

        return new LikedClubSearchPageResDto((int) res.getTotalElements(), res.getContent());
    }

    public LikedClubIdListResDto getLikedClubIdList(Long userId) {
        List<LikedClubIdResDto> res = likedClubRepository.searchClubIdList(userId);

        return new LikedClubIdListResDto(res.size(), res);
    }
}
