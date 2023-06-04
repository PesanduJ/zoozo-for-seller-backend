package com.zoozo.zoozoforsellers.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    private final String bucketName;
    private final String region;
    private final S3Client s3Client;

    public ImageService(@Value("${aws.s3.bucketName}") String bucketName,
                        @Value("${aws.s3.region}") String region,
                        @Autowired S3Client s3Client) {
        this.bucketName = bucketName;
        this.region = region;
        this.s3Client = s3Client;
    }

    public String saveImage(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String key = UUID.randomUUID().toString() + "/" + fileName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return key;
    }

    public ResponseBytes<GetObjectResponse> getImage(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        return s3Client.getObjectAsBytes(getObjectRequest);
    }

    public void cleanupFailedMultipartUploads() {
        ListMultipartUploadsRequest listMultipartUploadsRequest = ListMultipartUploadsRequest.builder()
                .bucket(bucketName)
                .build();

        ListMultipartUploadsResponse response = s3Client.listMultipartUploads(listMultipartUploadsRequest);

        List<MultipartUpload> uploads = response.uploads();
        for (MultipartUpload upload : uploads) {
            AbortMultipartUploadRequest abortRequest = AbortMultipartUploadRequest.builder()
                    .bucket(bucketName)
                    .key(upload.key())
                    .uploadId(upload.uploadId())
                    .build();

            s3Client.abortMultipartUpload(abortRequest);
        }
    }

}
