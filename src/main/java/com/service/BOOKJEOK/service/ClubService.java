package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.handler.ex.CustomApiException;
import com.service.BOOKJEOK.handler.ex.ExMessage;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import com.service.BOOKJEOK.repository.user.UserRepository;
import com.service.BOOKJEOK.repository.comment.CommentRepository;
import com.service.BOOKJEOK.repository.feed.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.service.BOOKJEOK.dto.club.ClubRequestDto.*;
import static com.service.BOOKJEOK.dto.club.ClubResponseDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubService {

    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final CommentRepository commentRepository;
    @Transactional
    public ClubCreateResDto createClub(ClubCreateReqDto requestDto) {
        User userPS = userRepository.findById(requestDto.getUserId()).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_USER));

        if(userPS.getClub() != null) {
            throw new CustomApiException(ExMessage.ALREADY_OWN_CLUB);
        }

        Club club = requestDto.toEntity(userPS);

        Club savedClub = clubRepository.save(club);

        return new ClubCreateResDto(savedClub);
    }

    public ClubSearchPageResDto searchClub(ClubSearchReqDto clubSearchReqDto, Pageable pageable) {

        Page<Club> clubs = clubRepository.searchClub(clubSearchReqDto, pageable);

        List<ClubSearchResDto> collect = clubs.stream().map(m ->
                new ClubSearchResDto(m)
        ).collect(Collectors.toList());

        return new ClubSearchPageResDto((int) clubs.getTotalElements(), collect);
    }

    public ClubSearchDetailResDto findClubById(Long clubId) {
        Club clubPS = clubRepository.findById(clubId).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_CLUB));

        ClubSearchDetailResDto clubSearchDetailResDto = new ClubSearchDetailResDto(clubPS);

        return clubSearchDetailResDto;
    }

    public ClubSearchDetailResDto findClubByUserId(Long userId) {
        Club clubPS = clubRepository.findByUserId(userId).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_CLUB));

        ClubSearchDetailResDto clubSearchDetailResDto = new ClubSearchDetailResDto(clubPS);

        return clubSearchDetailResDto;
    }

    @Transactional
    public void updateClub(ClubUpdateReqDto requestDto, Long userId) {
        Club clubPS = clubRepository.findByUserId(userId).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_CLUB));

        if(clubPS.getUser().getId() != userId) {
            throw new CustomApiException(ExMessage.FORBIDDEN);
        }

        clubPS.updateClub(requestDto);
    }

    @Transactional
    public void deleteClub(Long userId) {
        Club club = clubRepository.findByUserId(userId).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_CLUB));

        //연관된 member, likedClub 삭제
        clubRepository.deleteClub(club);
        List<Long> ids = feedRepository.findIdsByClub(club);
        //연관된 feed, comment, likedFeed 삭제
        feedRepository.deleteByFeedIds(ids);
    }

    public ClubSearchPageResDto searchClubForMain(String sortBy) {
        List<ClubSearchResDto> res = clubRepository.find4ClubList(sortBy);

        return new ClubSearchPageResDto(res.size(), res);
    }
}
