package com.service.BOOKJEOK.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserEnum {
    ADMIN("관리자", "ROLE_ADMIN"), USER("일반 유저", "ROLE_USER");

    public String value() {
        return role;
    }

    private String value;
    private String role;
}
