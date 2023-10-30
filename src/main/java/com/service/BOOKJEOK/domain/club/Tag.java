package com.service.BOOKJEOK.domain.club;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum Tag {

    SMALL("소모임"), OFFLINE("오프라인"), ONLINE("온라인"), CAPITAL("수도권"), LOCAL("지방");

    private String value;

    private static final Map<String, String> CODE_MAP =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(Tag::getValue, Tag::name)));

    public static Tag of(final String code) {
        return Tag.valueOf(CODE_MAP.get(code));
    }
}
