package com.service.BOOKJEOK.controller;

import com.service.BOOKJEOK.dto.ResponseDto;
import com.service.BOOKJEOK.dto.s3.S3ResponseDto;
import com.service.BOOKJEOK.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.service.BOOKJEOK.dto.s3.S3RequestDto.*;
import static com.service.BOOKJEOK.dto.s3.S3ResponseDto.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/s3")
public class S3Controller {

    private final S3Service s3Service;
    private static String path = "s3";

    @PostMapping
    public ResponseEntity<?> createUrl(@RequestBody S3UrlReqDto imageNameDTO) {
        path ="contact";  //원하는 경로 지정
        String imageName = imageNameDTO.getImageName();
        S3UrlResDto res = s3Service.getPreSignedUrl(path, imageName);

        return new ResponseEntity<>(new ResponseDto<>(1, "URL 생성 성공", res), HttpStatus.OK);
    }
}
