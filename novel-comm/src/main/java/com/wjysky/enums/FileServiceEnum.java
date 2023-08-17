package com.wjysky.enums;

import lombok.Getter;

/**
 * @author : 王俊元(wjysky520@gmail.com)
 * @ClassName : FileServiceEnum
 * @Description : TODO
 * @Date : 2023-08-16 14:59:09
 */
@Getter
public enum FileServiceEnum {

    LOCAL("local", "localService"),
    MINIO("minio", "minioService"),
    OSS("oss", "ossService");

    private final String code;

    private final String service;

    FileServiceEnum(String code, String service) {
        this.code = code;
        this.service = service;
    }

    public static String getFileService(String optTool) {
        for (FileServiceEnum serviceEnum : FileServiceEnum.values()) {
            if (serviceEnum.getCode().equals(optTool)) {
                return serviceEnum.getService();
            }
        }
        return null;
    }
}