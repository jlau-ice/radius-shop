package com.ice.service.impl;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URI;

@Slf4j
@Service
public class CosService {
    @Value("${storage.r2.endpoint}")
    private String endpoint;
    @Value("${storage.r2.access-key}")
    private String accessKey;
    @Value("${storage.r2.secret-key}")
    private String secretKey;
    @Value("${storage.r2.bucket}")
    private String bucket;
    @Value("${storage.r2.base-url}")
    private String baseUrl;

    private S3Client client;

    private S3Client getClient() {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    client = S3Client.builder()
                            .endpointOverride(URI.create(endpoint))
                            .region(Region.of("auto"))
                            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                            .build();
                }
            }
        }
        return client;
    }

    public String upload(MultipartFile file) {
        try {
            String originName = file.getOriginalFilename();
            String ext = originName != null && originName.contains(".") ?
                    originName.substring(originName.lastIndexOf(".")) : ".jpg";
            String key = IdUtil.fastSimpleUUID() + ext;

            PutObjectRequest req = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();
            getClient().putObject(req, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return baseUrl + "/" + key;
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new com.ice.common.exception.BusinessException("文件上传失败");
        }
    }
}
