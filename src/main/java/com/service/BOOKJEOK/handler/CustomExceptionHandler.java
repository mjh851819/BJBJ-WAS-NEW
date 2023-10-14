package com.service.BOOKJEOK.handler;

import com.service.BOOKJEOK.dto.ResponseDto;
import com.service.BOOKJEOK.handler.ex.CustomApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> apiException(CustomApiException e){
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> MissingServletRequestParameterException(MissingServletRequestParameterException e){
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, "잘못된 파라미터 입력입니다.", null), HttpStatus.BAD_REQUEST);
    }

}
