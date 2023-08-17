package com.wjysky.file.service.impl;

import com.wjysky.file.service.IFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pojo.bo.FileBO;
import pojo.vo.UploadFileVO;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @ClassName : OSSServiceImpl
 * @Description : TODO
 * @Author : 王俊元(wjysky520@gmail.com)
 * @Date : 2023-06-09 17:54:37
 */
@Service("ossService")
@Slf4j
@RequiredArgsConstructor
public class OSSServiceImpl implements IFileService {

    @Override
    public Boolean init() {
        return null;
    }

    @Override
    public Boolean getStatus() {
        return false;
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
        return null;
    }

    /**
     *
     * @ClassName OSSServiceImpl
     * @Title uploadFile
     * @Description 通过文件流上传
     * @param fileName 文件服务器上的文件名
     * @param ins 文件流
     * @author 王俊元(wangjy1@belink.com)
     * @Date 2023/8/11 11:48
     * @Return java.lang.String
     * @throws
    **/
    @Override
    public String uploadFile(String fileName, InputStream ins) {
        return null;
    }

    /**
     *
     * @ClassName OSSServiceImpl
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
     * @ClassName OSSServiceImpl
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
        return null;
    }
}