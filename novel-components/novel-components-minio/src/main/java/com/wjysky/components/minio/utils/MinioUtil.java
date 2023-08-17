package com.wjysky.components.minio.utils;

import com.wjysky.components.minio.config.MinioProperties;
import com.wjysky.enums.ErrorEnum;
import com.wjysky.exception.ApiException;
import io.minio.*;
import io.minio.http.Method;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * MinioClientUtil
 *
 * @author 王俊元（wjysky520@gmail.com）
 * @date 2023-03-09 16:36:13
 * @apiNote Minio存储服务客户端
 */
@Slf4j
public class MinioUtil {

    private static MinioClient client;

    private final String url;

    private final String accessKey;

    private final String secretKey;

    private final String bucket;

    @Getter
    private Boolean status;

    public MinioUtil(MinioProperties properties) {
        url = properties.getUrl();
        accessKey = properties.getAccessKey();
        secretKey = properties.getSecretKey();
        bucket = properties.getBucket();
    }

    synchronized
    public void changeStatus(boolean status) {
        this.status = status;
    }

    /**
     * @bizName 初始化Minio存储服务客户端
     *
     * @title init
     * @apiNote ${todo}
     * @param
     * @author 王俊元（wjysky520@gmail.com）
     * @date 2023/3/9 16:41
     * @return void
     **/
    public void init() {
        if (Boolean.TRUE.equals(this.status)) {
            return;
        }
        log.info("开始初始化Minio存储服务\nurl：{}\nbucket：{}", url, bucket);
        client = MinioClient.builder().endpoint(url).credentials(accessKey, secretKey).build();
        if (this.status == null) {
            makeBucket(bucket);
        }
        log.info("Minio存储服务初始化完毕");
        changeStatus(true); // 初始化成功
    }

    /**
     * @bizName 创建存储桶
     *
     * @title makeBucket
     * @apiNote 如果指定存储桶已存在则直接使用，不存在则创建
     * @param bucket
     * @author 王俊元（wjysky520@gmail.com）
     * @date 2023/3/9 16:41
     * @return void
     **/
    public void makeBucket(String bucket) {
        try {
            boolean bucketExist = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!bucketExist) {
                client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch (Exception e) {
            log.error("创建存储桶[{}]时异常", bucket, e);
            changeStatus(false);
            throw new ApiException(ErrorEnum.MINIO_MAKE_BUCKET_FAIL);
        }
    }

    public String uploadFile(String fileName, File file) throws Exception {
        return uploadFile(fileName, Files.newInputStream(file.toPath()), file.length());
    }

    public String uploadFile(String fileName, byte[] bytes) {
        return uploadFile(fileName, new ByteArrayInputStream(bytes), bytes.length);
    }

    /**
     * @bizName 上传文件
     *
     * @title uploadFile
     * @apiNote ${todo} 
     * @param fileName 上传服务器后的文件名
     * @param ins 文件流
     * @param fileSize 文件大小
     * @author 王俊元（wjysky520@gmail.com）
     * @date 2023/3/9 16:45
     * @return String
     **/
    public String uploadFile(String fileName, InputStream ins, long fileSize) {
        try {
            PutObjectArgs.Builder putObjectArgsBuilder = PutObjectArgs.builder().bucket(bucket)
                    .object(fileName).stream(ins, fileSize, 5 * 1024 * 1024); // minio存储分片除最后一片外其余分片最小限制为5MB
            ObjectWriteResponse response = client.putObject(putObjectArgsBuilder.build());
            return url + "/" + bucket + "/" + fileName;
        } catch (Exception e) {
            log.error("上传文件[{}]时异常", fileName, e);
            changeStatus(false);
            throw new ApiException(ErrorEnum.MINIO_UPLOAD_FILE_FAIL);
        }
    }

    /**
     * @bizName 下载文件
     *
     * @title downloadFile
     * @apiNote ${todo} 
     * @param fileName 服务器上的文件名，如果除存储桶还有其他路径则需带上剩余路径
     * @author 王俊元（wjysky520@gmail.com）
     * @date 2023/3/9 16:47
     * @return java.io.InputStream
     **/
    public InputStream downloadFile(String fileName) {
        try {
            GetObjectArgs.Builder getObjectArgsBuilder = GetObjectArgs.builder().bucket(bucket).object(fileName);
            return client.getObject(getObjectArgsBuilder.build());
        } catch (Exception e) {
            log.error("下载文件[{}]时异常", fileName, e);
            changeStatus(false);
            throw new ApiException(ErrorEnum.MINIO_DOWNLOAD_FILE_FAIL);
        }
    }

    /**
     * @bizName 获取服务器文件的签名url
     *
     * @title getMinioURL
     * @apiNote 有效期最大为7天
     * @param fileName 服务器上的文件名，如果除存储桶还有其他路径则需带上剩余路径
     * @author 王俊元（wjysky520@gmail.com）
     * @date 2023/8/16 11:47
     * @return java.lang.String
     **/
    public String getMinioURL(String fileName) {
        return getMinioURL(fileName, 7);
    }

    /**
     * @bizName 获取服务器文件的签名url
     *
     * @title getMinioURL
     * @apiNote 有效期最大为7天
     * @param fileName 服务器上的文件名，如果除存储桶还有其他路径则需带上剩余路径
     * @param expTime 有效时间，单位：秒，最大为：7 * 24 * 60 * 60
     * @author 王俊元（wjysky520@gmail.com）
     * @date 2023/3/9 16:52
     * @return java.lang.String
     **/
    public String getMinioURL(String fileName, Integer expTime) {
        try {
            if (expTime == null || expTime < 1 || expTime > 7) {
                log.error("传入有效时间有误：{}", expTime);
                throw new ApiException(ErrorEnum.MINIO_GET_URL_FAIL);
            }
            // 生成的预签名url可访问的有效时间，最大期限7天
            GetPresignedObjectUrlArgs build = GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucket).object(fileName).expiry(expTime).build();
            return client.getPresignedObjectUrl(build);
        } catch (Exception e) {
            log.error("获取[{}]的下载链接时异常", fileName, e);
            changeStatus(false);
            throw new ApiException(ErrorEnum.MINIO_GET_URL_FAIL);
        }
    }

}
