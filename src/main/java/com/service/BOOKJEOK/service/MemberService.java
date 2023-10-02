package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.member.Member;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.dto.member.MemberRequestDto;
import com.service.BOOKJEOK.handler.ex.CustomApiException;
import com.service.BOOKJEOK.handler.ex.ExMessage;
import com.service.BOOKJEOK.repository.MemberRepository;
import com.service.BOOKJEOK.repository.UserRepository;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.service.BOOKJEOK.dto.member.MemberRequestDto.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;

    @Transactional
    public void apply(MemberApplyReqDto request) {
        Club clubPS = clubRepository.findById(request.getClubId()).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_CLUB));
        User userPS = userRepository.findById(request.getUserId()).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_USER));

        if(clubPS.getUser().getId() == userPS.getId()) {
            throw new CustomApiException(ExMessage.IS_MY_CLUB);
        }

        if(memberRepository.findByUserAndClub(userPS, clubPS).isPresent()) {
            throw new CustomApiException(ExMessage.ALREADY_IN_CLUB);
        }

        Member myMember = Member.builder()
                .user(userPS)
                .club(clubPS)
                .build();

        memberRepository.save(myMember);
    }

    @Transactional
    public void delete(Long userId, Long clubId, Long myId) {
        User userPS = userRepository.findById(userId).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_CLUB));
        Club clubPS = clubRepository.findById(clubId).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_CLUB));

        if(!(userPS.getId().equals(myId) || clubPS.getUser().getId().equals(myId))) {
            throw new CustomApiException(ExMessage.FORBIDDEN);
        }

        Member memberPS = memberRepository.findByUserAndClub(userPS, clubPS).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_MEMBER));

        memberRepository.delete(memberPS);
    }
}
