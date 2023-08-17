package com.wjysky.file.controller;

import cn.hutool.core.bean.BeanUtil;
import com.wjysky.file.facade.IFileFacade;
import com.wjysky.pojo.ResultApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pojo.bo.FileBO;
import pojo.dto.UploadFileDTO;
import pojo.vo.UploadFileVO;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
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

    @PostMapping ("test")
    public ResultApi<List<UploadFileVO>> test() {
        List<FileBO> fileBOList = new ArrayList<>();
        try {
            fileBOList.add(new FileBO(1, new File("D:\\1.jpg"), System.currentTimeMillis() + "" + (new Random().nextInt(8999) + 1000) + ".jpg"));
            fileBOList.add(new FileBO(2, new File("D:\\2.jpg"), System.currentTimeMillis() + "" + (new Random().nextInt(8999) + 1000) + ".jpg"));
            List<UploadFileVO> fileVO = fileFacade.uploadFile(fileBOList);
            return ResultApi.generateSuccessMsg(fileVO);
        } catch (Exception e) {
            log.error("捕获到异常", e);
        }
        return ResultApi.generateFailMsg("上传失败");
    }

    @PostMapping("uploadFile")
    public ResultApi<List<UploadFileVO>> uploadFile(MultipartFile[] files, @RequestBody @Valid List<UploadFileDTO> fileDTOList) {
        List<FileBO> fileBOList = new ArrayList<>();
        // 读取RequestBody中传入的文件内容
        if (CollectionUtils.isNotEmpty(fileDTOList)) {
            fileBOList.addAll(fileDTOList.stream().map(fileDTO -> {
                FileBO fileBO = new FileBO();
                BeanUtil.copyProperties(fileDTO, fileBO);
                return fileBO;
            }).collect(Collectors.toList()));
        }
        // 获取当前最大的序号
        int sn = CollectionUtils.isNotEmpty(fileBOList) ? fileBOList.stream().max(Comparator.comparing(FileBO::getSn)).get().getSn() : 0;
        // 读取表单提交的文件
        for(MultipartFile file : files) {
            try {
                sn++; // 序号追加在RequestBody传入的文件序号后
                FileBO fileBO = new FileBO(sn, file.getBytes(), file.getName());
                fileBOList.add(fileBO);
            } catch (IOException e) {
                log.error("读取表单提交文件时异常");
            }
        }
        if (CollectionUtils.isEmpty(fileBOList)) {
            log.warn("没有需上传的文件");
            return ResultApi.generateFailMsg("没有需上传的文件");
        }
        List<UploadFileVO> fileVO = fileFacade.uploadFile(fileBOList);
        return ResultApi.generateSuccessMsg(fileVO);
    }
}
