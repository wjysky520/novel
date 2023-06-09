package com.wjysky.file.facade.impl;

import com.wjysky.components.minio.utils.MinioUtil;
import com.wjysky.file.facade.IFileFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import pojo.bo.FIleBO;
import pojo.vo.UploadFileVO;

import java.util.List;

/**
 * FileFacadeImpl
 *
 * @author 王俊元（wangjunyuan@talkweb.com.cn）
 * @date 2023-05-17 11:14:09
 * @apiNote
 */
@RequiredArgsConstructor
@Component
@Slf4j
@RefreshScope
public class FileFacadeImpl implements IFileFacade {

    @Value("${file.tool}")
    private String optTool;

    @Override
    public List<UploadFileVO> uploadFile(List<FIleBO> fileBOList) {

        return null;
    }
}
