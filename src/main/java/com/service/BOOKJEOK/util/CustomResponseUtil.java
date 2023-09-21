<<<<<<< HEAD
package com.service.BOOKJEOK.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.BOOKJEOK.dto.ExceptionResponseDto;
=======
package com.mjh8518.bank.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjh8518.bank.dto.ResponseDto;
>>>>>>> 36f437db1594839a77a680395ed75ec9deabc918
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

public class CustomResponseUtil {

    private static final Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);

    public static void unAuthentication(HttpServletResponse response, String msg) {
        try {
            ObjectMapper om = new ObjectMapper();
<<<<<<< HEAD
            ExceptionResponseDto responseDto = new ExceptionResponseDto(-1, msg, null);
=======
            ResponseDto responseDto = new ResponseDto<>(-1, msg, null);
>>>>>>> 36f437db1594839a77a680395ed75ec9deabc918
            String responseBody = om.writeValueAsString(responseDto);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(401);
            //response.getWriter().println("error"); // 모든 요청의 응답에서 "error" 로 일관성 있는 응답을 만들었다. dto로 깔끔하게 만들어보자.
            response.getWriter().println(responseBody);
        } catch (Exception e){
<<<<<<< HEAD
            log.error("Server Parsing Error");
=======
            log.error("서버 파싱 에러");
>>>>>>> 36f437db1594839a77a680395ed75ec9deabc918
        }

    }
}
