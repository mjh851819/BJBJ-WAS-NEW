package com.service.BOOKJEOK.util.dummy;

import com.service.BOOKJEOK.domain.User;

public class DummyObject {

    protected User newMockUser(Long id, String name, String email) {
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .build();
    }
}
