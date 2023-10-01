package com.service.BOOKJEOK.util.dummy;

import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.domain.user.UserEnum;
import com.service.BOOKJEOK.dto.club.ClubRequestDto;

import static com.service.BOOKJEOK.dto.club.ClubRequestDto.*;

public class DummyObject {

    protected User newMockUser(Long id, String name, String email) {
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .role(UserEnum.USER)
                .build();
    }

    protected Club newMockClub(Long id, String title, User user) {
        return Club.builder()
                .user(user)
                .id(id)
                .title(title)
                .tags("온라인,오프라인")
                .build();
    }
}
