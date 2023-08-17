package com.wjysky.file.facade.impl;

import com.wjysky.file.facade.IFileFacade;
import com.wjysky.file.service.IFileService;
import com.wjysky.file.strategy.FileStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import pojo.bo.FileBO;
import pojo.vo.UploadFileVO;

import java.util.List;

/**
 * FileFacadeImpl
 *
 * @author 王俊元（wjysky520@gmail.com）
 * @date 2023-05-17 11:14:09
 * @apiNote
 */
@RequiredArgsConstructor
@Component
@Slf4j
@RefreshScope
public class FileFacadeImpl implements IFileFacade {

    private final FileStrategy fileStrategy;

    @Override
    public List<UploadFileVO> uploadFile(List<FileBO> fileBOList) {
        return fileStrategy.uploadFile(fileBOList);
    }
}
