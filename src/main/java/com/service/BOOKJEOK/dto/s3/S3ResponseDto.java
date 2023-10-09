package com.service.BOOKJEOK.dto.s3;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class S3ResponseDto {

    @Getter
    @AllArgsConstructor
    static public class S3UrlResDto {
        private String Url;
    }
}
