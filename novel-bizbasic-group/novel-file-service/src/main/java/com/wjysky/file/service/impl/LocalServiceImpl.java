package com.wjysky.file.service.impl;

import com.wjysky.enums.ErrorEnum;
import com.wjysky.exception.ApiException;
import com.wjysky.file.service.IFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;

/**
 * @ClassName : LocalServiceImpl
 * @Description : TODO
 * @Author : 王俊元(wjysky520@gmail.com)
 * @Date : 2023-06-09 17:57:27
 */
@Service("localService")
@Slf4j
@RequiredArgsConstructor
public class LocalServiceImpl implements IFileService {

    @Value("${file.upload-path}")
    private String uploadPath;

    @Value("${file.download-link}")
    private String downloadLink;

    @Override
    public Boolean init() {
        File folder = new File(uploadPath);
        if (!folder.exists() || !folder.isDirectory()) {
            return new File(uploadPath).mkdirs();
        }
        return true;
    }

    @Override
    public Boolean getStatus() {
        return true;
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
    public String uploadFile(String fileName, File file) {
        try {
            LocalDate nowDate = LocalDate.now();
            String folderPath = createFolderPath(nowDate);
            String filepath = folderPath + fileName;
            FileUtils.copyFile(file, new File(filepath));
            return downloadLink + "/" + nowDate.getYear() + "/" + nowDate.getMonth() + "/" + nowDate.getDayOfMonth() + "/" + fileName;
        } catch (Exception e) {
            log.error("上传文件时异常", e);
            throw new ApiException(ErrorEnum.MINIO_UPLOAD_FILE_FAIL);
        }
    }

    /**
     *
     * @ClassName LocalServiceImpl
     * @Title uploadFile
     * @Description 通过文件流上传
     * @param fileName 文件服务器上的文件名
     * @param ins 文件流
     * @author 王俊元(wangjy1@belink.com)
     * @Date 2023/8/11 11:47
     * @Return java.lang.String
     * @throws
    **/
    @Override
    public String uploadFile(String fileName, InputStream ins) {
        return null;
    }

    /**
     *
     * @ClassName LocalServiceImpl
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
        return null;
    }

    /**
     *
     * @ClassName LocalServiceImpl
     * @Title downloadFile
     * @Description 下载文件，返回文件流
     * @param fileName 文件服务器上的文件名
     * @author 王俊元(wangjy1@belink.com)
     * @Date 2023/8/11 11:48
     * @Return java.io.InputStream
     * @throws
    **/
    @Override
    public InputStream downloadFile(String fileName) {
        return null;
    }

    private String createFolderPath(LocalDate nowDate) {
        String folderPath = uploadPath + File.separator + nowDate.getYear() + File.separator
                + nowDate.getMonth() + File.separator + nowDate.getDayOfMonth() + File.separator;
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            folder.mkdirs();
        }
        return folderPath;
    }
}