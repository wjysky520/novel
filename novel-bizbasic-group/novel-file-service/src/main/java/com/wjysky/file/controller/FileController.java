package com.wjysky.file.controller;

import cn.hutool.core.bean.BeanUtil;
import com.wjysky.file.facade.IFileFacade;
import com.wjysky.pojo.ResultApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pojo.bo.FIleBO;
import pojo.dto.UploadFileDTO;
import pojo.vo.UploadFileVO;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FileController
 *
 * @author 王俊元（wjysky520@gmail.com）
 * @date 2023-05-17 09:09:43
 * @apiNote 文件处理接口
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/file", method = RequestMethod.POST)
@Slf4j
public class FileController {

    private final IFileFacade fileFacade;

    @PostMapping("uploadFile")
    public ResultApi<List<UploadFileVO>> uploadFile(MultipartFile[] files, @RequestBody @Valid List<UploadFileDTO> fileDTOList) {
        List<FIleBO> fileBOList = new ArrayList<>();
        for(MultipartFile file : files) {
            try {
                FIleBO fIleBO = new FIleBO(file.getBytes(), file.getName());
                fileBOList.add(fIleBO);
            } catch (IOException e) {
                log.error("读取表单提交文件时异常");
            }
        }
        if (CollectionUtils.isNotEmpty(fileDTOList)) {
            fileBOList.addAll(fileDTOList.stream().map(fileDTO -> {
                FIleBO fIleBO = new FIleBO();
                BeanUtil.copyProperties(fileDTO, fIleBO);
                return fIleBO;
            }).collect(Collectors.toList()));
        }
        if (CollectionUtils.isEmpty(fileBOList)) {
            log.warn("");
        }
        List<UploadFileVO> fileVO = fileFacade.uploadFile(fileBOList);
        return ResultApi.generateSuccessMsg(fileVO);
    }
}
