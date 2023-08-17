package com.wjysky.enums;

import lombok.Getter;

/**
 * @author : 王俊元(wjysky520@gmail.com)
 * @ClassName : ErrorEnum
 * @Description : 错误信息枚举类
 * @Date : 2023-08-14 10:28:39
 */
@Getter
public enum ErrorEnum {

    SUCCESS(0, "success"), // 成功
    ERROR(1, "error"), // 失败

    /* ---------- 1开头为框架错误信息 S ----------*/
    NOT_FOUND_FILE_SERVICE(10101, "未找到可用的文件服务"),
    NOT_FOUND_STRATEGY_IMPL(10102, "未找到策略模式实现类"),
    NOT_FOUND_UPLOAD_FILE(10103, "未找到需上传的文件"),
    /* ---------- 1开头为框架错误信息 E ----------*/

    /* ---------- 2开头为组件错误信息 S ----------*/
    UPLOAD_FILE_FAIL(20001, "上传文件失败"),
    FILENAME_ILLEGALITY(20002, "文件名称不符合规则"),

    MINIO_MAKE_BUCKET_FAIL(20101, "Minio存储服务创建存储桶失败"),
    MINIO_UPLOAD_FILE_FAIL(20102, "Minio存储服务上传文件失败"),
    MINIO_DOWNLOAD_FILE_FAIL(20103, "Minio存储服务下载文件失败"),
    MINIO_GET_URL_FAIL(20104, "Minio存储服务获取下载链接失败"),
    /* ---------- 2开头为组件错误信息 E ----------*/

    /* ---------- 3开头为公共服务错误信息 S ----------*/
    NOT_FOUND_LOCAL_FILE(30101, "没有找到对应的本地文件"),

    NOT_SUPPORT_DATE_UNIT(30201, "暂不支持该时间类型"),
    /* ---------- 3开头为公共服务错误信息 E ----------*/
    ;



    private final int code;

    private final String msg;

    ErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}