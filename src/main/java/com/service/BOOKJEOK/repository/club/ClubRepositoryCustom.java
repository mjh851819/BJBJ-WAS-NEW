package com.service.BOOKJEOK.repository.club;

import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.dto.club.ClubRequestDto;
import com.service.BOOKJEOK.dto.club.ClubResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.service.BOOKJEOK.dto.club.ClubRequestDto.*;
import static com.service.BOOKJEOK.dto.club.ClubResponseDto.*;

public interface ClubRepositoryCustom {

    Page<Club> searchClub(ClubSearchReqDto clubSearchReqDto, Pageable pageable);
}
