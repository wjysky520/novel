package com.wjysky.file.strategy;

import com.wjysky.components.idgenerator.IdGenerator;
import com.wjysky.enums.ErrorEnum;
import com.wjysky.enums.FileServiceEnum;
import com.wjysky.exception.ApiException;
import com.wjysky.file.service.IFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import pojo.bo.FileBO;
import pojo.vo.UploadFileVO;

import java.io.File;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @ClassName : FileStrategy
 * @Description : TODO
 * @Author : 王俊元(wjysky520@gmail.com)
 * @Date : 2023-06-09 17:59:12
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class FileStrategy {

    @Value("${file.server}")
    private String optServer; // 预设的文件存储服务，local-本地存储，minio-minio文件存储服务，oss-阿里云OSS文件存储服务

    private final Map<String, IFileService> fileServiceMap; // spring框架会自动注入IFileService接口的实现类，key为bean_id，value为对应实现类

    private final ThreadPoolTaskExecutor uploadFileTaskExecutor;

    private final IdGenerator idGenerator;

    /**
     *
     * @ClassName FileStrategy
     * @Title createFileService
     * @Description 根据文件服务名称生成对应的文件服务实现类
     * @param serviceName 文件服务名称
     * @author 王俊元(wangjy1@belink.com)
     * @Date 2023/8/16 15:12
     * @Return com.wjysky.file.service.IFileService
     * @throws 
    **/
    public IFileService createFileService(String serviceName) {
        IFileService fileService = fileServiceMap.get(serviceName);
        if (fileService == null) {
            log.error("未找到实现类：{}", serviceName);
            throw new ApiException(ErrorEnum.NOT_FOUND_STRATEGY_IMPL);
        }
        return fileService;
    }

    /**
     *
     * @ClassName FileStrategy
     * @Title createFileService
     * @Description 生成对应的文件服务实现类，如果没有预设服务则默认依次使用minio-oss-local
     * @param 
     * @author 王俊元(wangjy1@belink.com)
     * @Date 2023/8/16 15:13
     * @Return com.wjysky.file.service.IFileService
     * @throws 
    **/
    public IFileService createFileService() {
        // 默认使用预设的文件服务
        if (StringUtils.isNotBlank(optServer) && StringUtils.isNotBlank(FileServiceEnum.getFileService(optServer))) {
            String serviceName = FileServiceEnum.getFileService(optServer);
            return createFileService(serviceName);
        }
        // 依次使用minio -> oss -> local
        IFileService fileService = fileServiceMap.get(FileServiceEnum.MINIO.getService());
        if (fileService != null && fileService.getStatus()) {
            return fileService;
        }
        fileService = fileServiceMap.get(FileServiceEnum.OSS.getService());
        if (fileService != null && fileService.getStatus()) {
            return fileService;
        }
        fileService = fileServiceMap.get(FileServiceEnum.LOCAL.getService());
        if (fileService != null && fileService.getStatus()) {
            return fileService;
        }
        log.error("未找到可用的文件存储服务");
        throw new ApiException(ErrorEnum.NOT_FOUND_FILE_SERVICE);
    }

    /**
     *
     * @ClassName FileStrategy
     * @Title uploadFile
     * @Description 单个文件上传至服务器，使用的存储服务会随策略不同而改变
     * @param fileBO 文件信息对象
     * @author 王俊元(wangjy1@belink.com)
     * @Date 2023/8/17 16:11
     * @Return java.lang.String
     * @throws 
    **/
    public String uploadFile(FileBO fileBO) {
        IFileService fileService = createFileService();
        try {
            String filename = getTargetFileName(fileBO.getFilename());
            File file = fileBO.getFile();
            byte[] bytes = fileBO.getBytes();
            String base64 = fileBO.getBase64();
            if (file.exists() && file.isFile()) {
                return fileService.uploadFile(filename, file);
            } else if (StringUtils.isNotBlank(base64)) {
                return fileService.uploadFile(filename, Base64.getDecoder().decode(base64));
            } else if (bytes.length > 0) {
                return fileService.uploadFile(filename, file);
            }
        } catch (Exception e) {
            log.error("上传文件时异常", e);
        }
        throw new ApiException(ErrorEnum.MINIO_UPLOAD_FILE_FAIL);
    }

    /**
     *
     * @ClassName FileStrategy
     * @Title uploadFile
     * @Description 批量文件上传，使用的存储服务会随策略不同而改变
     * @param fileBOList 文件信息对象集合
     * @author 王俊元(wangjy1@belink.com)
     * @Date 2023/8/17 16:12
     * @Return java.util.List<pojo.vo.UploadFileVO>
     * @throws 
    **/
    public List<UploadFileVO> uploadFile(List<FileBO> fileBOList) {
        if (CollectionUtils.isEmpty(fileBOList)) {
            log.error("上传文件集合为空");
            throw new ApiException(ErrorEnum.NOT_FOUND_UPLOAD_FILE);
        }
        List<CompletableFuture<UploadFileVO>> futureList = fileBOList.stream().map(fileBO ->
                // 同步调用线程池完成上传文件
                CompletableFuture.supplyAsync(() -> new UploadFileVO(fileBO.getSn(), uploadFile(fileBO)), uploadFileTaskExecutor)
        ).collect(Collectors.toList());
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{}));
        return futureList.stream().map(future -> {
            try {
                // 解析上传结果
                return future.get();
            } catch (Exception e) {
                log.error("解析上传结果时异常", e);
                throw new ApiException(ErrorEnum.UPLOAD_FILE_FAIL);
            }
        }).collect(Collectors.toList());
    }

    /**
     *
     * @ClassName FileStrategy
     * @Title getTargetFileName
     * @Description 生成雪花算法的文件名
     * @param filename 原文件名
     * @author 王俊元(wangjy1@belink.com)
     * @Date 2023/8/17 16:13
     * @Return java.lang.String
     * @throws 
    **/
    private String getTargetFileName(String filename) {
        String[] fileArray = filename.split("\\.");
        if (fileArray.length < 2) {
            log.error("文件名称不符合规则：{}", filename);
            throw new ApiException(ErrorEnum.FILENAME_ILLEGALITY);
        }
        return idGenerator.getSnowId() + "." + fileArray[fileArray.length - 1];
    }
}