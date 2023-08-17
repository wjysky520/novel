package com.wjysky.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

@Slf4j
public class FileUtil {

    public static String file2Base64(File file) {
        if (file == null) {
            return null;
        }
        try {
            byte[] b = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            return Base64.getEncoder().encodeToString(b);
        } catch (Exception e) {
            log.error("文件转Base64时异常", e);
        }
        return null;
    }

    public static byte[] file2Bytes(File file) {
        if (file == null) {
            return null;
        }
        try {
            return Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        } catch (Exception e) {
            log.error("文件转byte数组时异常", e);
        }
        return null;
    }

    public static boolean base642File(String base64, String filePath) {
        if (StringUtils.isBlank(base64) || StringUtils.isBlank(filePath)) {
            return false;
        }
        try {
            Files.write(Paths.get(filePath), Base64.getDecoder().decode(base64), StandardOpenOption.CREATE);
            return true;
        } catch (Exception e) {
            log.error("Base64转文件时异常", e);
        }
        return false;
    }

    public static byte[] base642Bytes(String base64) {
        if (StringUtils.isBlank(base64)) {
            return null;
        }
        try {
            return Base64.getDecoder().decode(base64.trim());
        } catch (Exception e) {
            log.error("Base64转byte数组时异常", e);
        }
        return null;
    }

    public static boolean bytes2File(byte[] bytes, String filePath) {
        if (bytes == null || bytes.length <= 0 || StringUtils.isBlank(filePath)) {
            return false;
        }
        try {
            Files.write(Paths.get(filePath), bytes, StandardOpenOption.CREATE);
            return true;
        } catch (Exception e) {
            log.error("Base64转文件时异常", e);
        }
        return false;
    }

    public static String bytes2Base64(byte[] bytes) {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        try {
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            log.error("Base64转文件时异常", e);
        }
        return null;
    }

    public static void main(String[] args) {
        File file = new File("D:\\1.jpg");
        log.info("------" + File.separator);
        log.info("======" + File.pathSeparator);
    }
}
