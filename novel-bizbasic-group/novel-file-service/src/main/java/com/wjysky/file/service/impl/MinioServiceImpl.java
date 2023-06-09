package com.wjysky.file.service.impl;

import com.wjysky.components.minio.utils.MinioUtil;
import com.wjysky.file.service.IFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * MinioServiceImpl
 *
 * @author 王俊元（wangjunyuan@talkweb.com.cn）
 * @date 2023-05-17 11:22:45
 * @apiNote
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MinioServiceImpl implements IFileService {

    private final MinioUtil minioUtil;
}
