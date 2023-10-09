package com.service.BOOKJEOK.dto.s3;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class S3RequestDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class S3UrlReqDto{
        private String imageName;
    }
}
