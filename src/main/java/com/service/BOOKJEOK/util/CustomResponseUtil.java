package com.mjh8518.bank.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjh8518.bank.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

public class CustomResponseUtil {

    private static final Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);

    public static void unAuthentication(HttpServletResponse response, String msg) {
        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto responseDto = new ResponseDto<>(-1, msg, null);
            String responseBody = om.writeValueAsString(responseDto);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(401);
            //response.getWriter().println("error"); // 모든 요청의 응답에서 "error" 로 일관성 있는 응답을 만들었다. dto로 깔끔하게 만들어보자.
            response.getWriter().println(responseBody);
        } catch (Exception e){
            log.error("서버 파싱 에러");
        }

    }
}
