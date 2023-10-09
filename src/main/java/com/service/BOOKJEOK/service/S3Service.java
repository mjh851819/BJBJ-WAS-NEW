package com.service.BOOKJEOK.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.service.BOOKJEOK.dto.s3.S3ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

import static com.service.BOOKJEOK.dto.s3.S3ResponseDto.*;

@RequiredArgsConstructor
@Service
public class S3Service {

    private final AmazonS3 amazonS3;

    private String useOnlyOneFileName;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3UrlResDto getPreSignedUrl(String prefix, String fileName) {

        String onlyOneFileName = onlyOneFileName(fileName);

        useOnlyOneFileName = onlyOneFileName;

        if (!prefix.equals("")) {
            onlyOneFileName = prefix + "/" + onlyOneFileName;
        }
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(bucket, onlyOneFileName);

        String url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();

        return new S3UrlResDto(url);
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String bucket, String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, fileName)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    private String onlyOneFileName(String filename){
        return UUID.randomUUID().toString()+filename;

    }
}
