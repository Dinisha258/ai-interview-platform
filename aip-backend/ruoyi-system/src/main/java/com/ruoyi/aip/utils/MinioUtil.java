package com.ruoyi.aip.utils;

import com.ruoyi.common.utils.DateUtils;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@Component
public class MinioUtil {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.bucketName}")
    private String bucketName;

    /**
     * 上传文件并返回绝对 URL
     */
    public String uploadFile(MultipartFile file) throws Exception {
        // 生成唯一文件名，防止覆盖 (格式：20260401_uuid.wav)
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = DateUtils.datePath() + "_" + UUID.randomUUID().toString().replace("-", "") + extension;

        // 执行上传
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        // 拼接出URL
        return endpoint + "/" + bucketName + "/" + fileName;
    }
}
