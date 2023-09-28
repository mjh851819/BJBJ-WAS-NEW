package com.service.BOOKJEOK.domain.club;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ClubStatus {
    ACTIVE("모집중"), EXPIRED("마감됨");

    private String value;
}
