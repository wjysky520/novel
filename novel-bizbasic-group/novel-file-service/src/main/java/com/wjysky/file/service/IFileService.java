package com.wjysky.file.service;

import pojo.bo.FileBO;
import pojo.vo.UploadFileVO;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * IMinioService
 *
 * @author 王俊元（wjysky520@gmail.com）
 * @Date 2023-05-17 11:22:08
 * @apiNote 文件服务接口
 */
public interface IFileService {

    public Boolean init();

    public Boolean getStatus();

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
    public String uploadFile(String fileName, File file);

    /**
     *
     * @ClassName IFileService
     * @Title uploadFile
     * @Description 通过文件流上传
     * @param fileName 文件服务器上的文件名
     * @param ins 文件流
     * @author 王俊元(wangjy1@belink.com)
     * @Date 2023/8/11 11:41
     * @Return java.lang.String
     * @throws
    **/
    public String uploadFile(String fileName, InputStream ins);

    /**
     *
     * @ClassName IFileService
     * @Title uploadFile
     * @Description 通过byte数组上传
     * @param fileName 文件服务器上的文件名
     * @param bytes byte数组
     * @author 王俊元(wangjy1@belink.com)
     * @Date 2023/8/11 11:44
     * @Return java.lang.String
     * @throws
    **/
    public String uploadFile(String fileName, byte[] bytes);

    /**
     *
     * @ClassName IFileService
     * @Title download
     * @Description 下载文件，返回文件流
     * @param fileName 文件服务器上的文件名
     * @author 王俊元(wangjy1@belink.com)
     * @Date 2023/8/11 11:45
     * @Return java.io.InputStream
     * @throws 
    **/
    public InputStream downloadFile(String fileName);
}
