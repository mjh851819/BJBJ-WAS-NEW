package com.service.BOOKJEOK.repository.likedclub;

import com.service.BOOKJEOK.domain.member.ApprovalStatus;
import com.service.BOOKJEOK.dto.likedclub.LikedClubResponseDto;
import com.service.BOOKJEOK.dto.member.MemberResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.service.BOOKJEOK.dto.likedclub.LikedClubResponseDto.*;

public interface LikedClubRepositoryCustom {
    Page<LikedClubSearchResDto> searchClubList(Long userId, Pageable pageable);

    List<LikedClubIdResDto> searchClubIdList(Long userId);
}
