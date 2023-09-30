package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.handler.ex.CustomApiException;
import com.service.BOOKJEOK.handler.ex.ExMessage;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import com.service.BOOKJEOK.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.service.BOOKJEOK.dto.club.ClubRequestDto.*;
import static com.service.BOOKJEOK.dto.club.ClubResponseDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubService {

    private final ClubRepository clubRepository;
    private final UserRepository userRepository;

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
                ClubSearchResDto.builder()
                        .id(m.getId())
                        .title(m.getTitle())
                        .contents(m.getContents())
                        .img_url(m.getImg_url())
                        .tags(m.getTags())
                        .likes(m.getLikes())
                        .build()
        ).collect(Collectors.toList());

        return new ClubSearchPageResDto((int) clubs.getTotalElements(), collect);
    }
}
