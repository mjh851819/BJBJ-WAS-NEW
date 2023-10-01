package com.service.BOOKJEOK.domain.club;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Tag {

    SMALL("소모임"), OFFLINE("오프라인"), ONLINE("온라인"), CAPITAL("수도권"), LOCAL("지방");

    private String value;
}
