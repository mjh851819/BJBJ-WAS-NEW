package com.service.BOOKJEOK.repository.member;

import com.service.BOOKJEOK.domain.member.ApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.service.BOOKJEOK.dto.member.MemberResponseDto.*;

public interface MemberRepositoryCustom {
    Page<MemberSearchResDto> searchMember(Long userId, ApprovalStatus status, Pageable pageable);

    Page<MemberJoiningClubResDto> searchJoiningClubs(Long userId, Pageable pageable);

    List<MemberJoiningClubsIdResDto> searchJoiningClubIds(Long userId, ApprovalStatus status);
}
