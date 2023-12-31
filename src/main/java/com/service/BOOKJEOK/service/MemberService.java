package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.member.ApprovalStatus;
import com.service.BOOKJEOK.domain.member.Member;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.handler.ex.CustomApiException;
import com.service.BOOKJEOK.handler.ex.ExMessage;
import com.service.BOOKJEOK.repository.member.MemberRepository;
import com.service.BOOKJEOK.repository.user.UserRepository;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.service.BOOKJEOK.dto.member.MemberRequestDto.*;
import static com.service.BOOKJEOK.dto.member.MemberResponseDto.*;

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
    public void delete(Long userId, Long clubId) {
        User userPS = userRepository.findById(userId).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_CLUB));
        Club clubPS = clubRepository.findById(clubId).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_CLUB));

        Member memberPS = memberRepository.findByUserAndClub(userPS, clubPS).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_MEMBER));

        memberRepository.delete(memberPS);
    }

    @Transactional
    public void approve(MemberApproveReqDto req) {
        Member memberPS = memberRepository.findById(req.getMemberId()).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_MEMBER));
        if(memberPS.getStatus() == ApprovalStatus.CONFIRMED) {
            throw new CustomApiException(ExMessage.ALREADY_CONFIRMED_CLUB);
        }

        memberPS.updateState();
    }

    public MemberSearchPageResDto getMemberList(Long userId, String approvalStatus, Pageable pageable) {
        Club clubPS = clubRepository.findByUserId(userId).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_CLUB));
        Page<MemberSearchResDto> res = memberRepository.searchMember(
                clubPS.getId(),
                approvalStatus.equals(ApprovalStatus.CONFIRMED.getValue()) ? ApprovalStatus.CONFIRMED : ApprovalStatus.WAITING,
                pageable
        );

        return new MemberSearchPageResDto((int) res.getTotalElements(), res.getContent());
    }

    public MemberJoiningClubsPageResDto getJoiningClubList(Long userId, Pageable pageable) {
        Page<MemberJoiningClubResDto> res = memberRepository.searchJoiningClubs(userId, pageable);

        return new MemberJoiningClubsPageResDto((int) res.getTotalElements(), res.getContent());
    }

    public MemberJoiningClubsIdListResDto getJoiningClubIds(Long userId) {

        List<MemberJoiningClubsIdResDto> res = memberRepository.searchJoiningClubIds(userId);

        return new MemberJoiningClubsIdListResDto(res.size(), res);
    }
}
