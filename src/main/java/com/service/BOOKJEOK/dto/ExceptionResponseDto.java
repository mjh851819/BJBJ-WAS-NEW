<<<<<<< HEAD
package com.service.BOOKJEOK.dto;
=======
package com.mjh8518.bank.dto;
>>>>>>> 36f437db1594839a77a680395ed75ec9deabc918

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
<<<<<<< HEAD
public class ExceptionResponseDto<T> {
=======
public class ResponseDto<T> {
>>>>>>> 36f437db1594839a77a680395ed75ec9deabc918

    private final Integer code; // 1 성공, -1 실패
    private final String msg;
    private final T data;
}
