package com.service.BOOKJEOK.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ExceptionResponseDto<T> {

    private final Integer code; // 1 성공, -1 실패
    private final String msg;
    private final T data;
}
