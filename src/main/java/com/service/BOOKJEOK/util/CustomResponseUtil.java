package com.service.BOOKJEOK.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.service.BOOKJEOK.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

public class CustomResponseUtil {

    private static final Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);

    public static void fail(HttpServletResponse response, String msg, HttpStatus code) {
        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto responseDto = new ResponseDto<>(-1, msg, null);
            String responseBody = om.writeValueAsString(responseDto);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(code.value());
            //response.getWriter().println("error"); // 모든 요청의 응답에서 "error" 로 일관성 있는 응답을 만들었다. dto로 깔끔하게 만들어보자.
            response.getWriter().println(responseBody);
        } catch (Exception e){
            log.error("서버 파싱 에러");
        }
    }

    public static void success(HttpServletResponse response, Object dto) {
        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto responseDto = new ResponseDto<>(1, "로그인 성공", dto);
            String responseBody = om.writeValueAsString(responseDto);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(200);
            response.getWriter().println(responseBody);
        } catch (Exception e){
            log.error("서버 파싱 에러");
        }
    }
}
