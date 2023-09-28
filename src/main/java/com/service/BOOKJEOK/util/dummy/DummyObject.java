package com.service.BOOKJEOK.util.dummy;

import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.dto.club.ClubRequestDto;

import static com.service.BOOKJEOK.dto.club.ClubRequestDto.*;

public class DummyObject {

    protected User newMockUser(Long id, String name, String email) {
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .build();
    }

    protected Club newMockClub(Long id, String title) {
        return Club.builder()
                .id(id)
                .title(title)
                .build();
    }
}
