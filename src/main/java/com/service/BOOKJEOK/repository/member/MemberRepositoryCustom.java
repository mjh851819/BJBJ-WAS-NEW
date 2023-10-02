package com.service.BOOKJEOK.repository.member;

import com.service.BOOKJEOK.domain.member.ApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.service.BOOKJEOK.dto.member.MemberResponseDto.*;

public interface MemberRepositoryCustom {
    Page<MemberSearchResDto> searchMember(Long userId, ApprovalStatus status, Pageable pageable);
}
