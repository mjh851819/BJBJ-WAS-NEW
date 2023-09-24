package com.service.BOOKJEOK.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserEnum {
    ADMIN("관리자"), USER("일반 유저");

    private String value;
}
