package com.wjysky.file.service.impl;

import com.wjysky.components.minio.utils.MinioUtil;
import com.wjysky.enums.ErrorEnum;
import com.wjysky.exception.ApiException;
import com.wjysky.file.service.IFileService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pojo.bo.FileBO;
import pojo.vo.UploadFileVO;

import java.io.File;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MinioServiceImpl
 *
 * @author 王俊元（wjysky520@gmail.com）
 * @date 2023-05-17 11:22:45
 * @apiNote
 */
@Service("minioService")
@Slf4j
@RequiredArgsConstructor
public class MinioServiceImpl implements IFileService {

    private final MinioUtil minioUtil;

    @Override
    public Boolean init() {
        minioUtil.init();
        return minioUtil.getStatus();
    }

    @Override
    public Boolean getStatus() {
        return minioUtil.getStatus();
    }

    /**
     *
     * @ClassName IFileService
     * @Title uploadFile
     * @Description 通过文件对象上传
     * @param fileName 文件服务器上的文件名
     * @param file 文件对象
     * @author 王俊元(wangjy1@belink.com)
     * @Date 2023/8/11 11:40
     * @Return java.lang.String
     * @throws
     **/
    @Override
    public String uploadFile(@NonNull String fileName, @NonNull File file) {
        try {
            if (!file.isFile()) {
                log.error(ErrorEnum.NOT_FOUND_LOCAL_FILE.getMsg() + "：{}", file.getAbsolutePath());
                throw new ApiException(ErrorEnum.NOT_FOUND_LOCAL_FILE);
            }
            return minioUtil.uploadFile(fileName, file);
        } catch (Exception e) {
            log.error("上传文件时异常", e);
            throw new ApiException(ErrorEnum.MINIO_UPLOAD_FILE_FAIL);
        }
    }

    /**
     *
     * @ClassName MinioServiceImpl
     * @Title uploadFile
     * @Description 通过文件流上传
     * @param fileName 文件服务器上的文件名
     * @param ins 文件流，不可使用网络文件流
     * @author 王俊元(wangjy1@belink.com)
     * @Date 2023/8/11 11:47
     * @Return java.lang.String
     * @throws
    **/
    @Override
    public String uploadFile(@NonNull String fileName, @NonNull InputStream ins) {
        try {
            return minioUtil.uploadFile(fileName, ins, ins.available());
        } catch (Exception e) {
            log.error("上传文件时异常", e);
            throw new ApiException(ErrorEnum.MINIO_UPLOAD_FILE_FAIL);
        }
    }

    /**
     *
     * @ClassName MinioServiceImpl
     * @Title uploadFile
     * @Description 通过byte数组上传
     * @param fileName 文件服务器上的文件名
     * @param bytes byte数组
     * @author 王俊元(wangjy1@belink.com)
     * @Date 2023/8/11 11:48
     * @Return java.lang.String
     * @throws
    **/
    @Override
    public String uploadFile(String fileName, byte[] bytes) {
        try {
            return minioUtil.uploadFile(fileName, bytes);
        } catch (Exception e) {
            log.error("上传文件时异常", e);
            throw new ApiException(ErrorEnum.MINIO_UPLOAD_FILE_FAIL);
        }
    }

    /**
     *
     * @ClassName MinioServiceImpl
     * @Title downloadFile
     * @Description 下载文件，返回文件流
     * @param fileName 文件服务器上的文件名
     * @author 王俊元(wangjy1@belink.com)
     * @Date 2023/8/11 11:49
     * @Return java.io.InputStream
     * @throws
    **/
    @Override
    public InputStream downloadFile(String fileName) {
        try {
            return minioUtil.downloadFile(fileName);
        } catch (Exception e) {
            log.error("下载文件时异常", e);
            throw new ApiException(ErrorEnum.MINIO_DOWNLOAD_FILE_FAIL);
        }
    }
}
