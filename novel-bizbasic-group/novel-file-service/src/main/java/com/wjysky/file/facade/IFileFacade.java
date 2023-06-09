package com.wjysky.file.facade;

import pojo.bo.FIleBO;
import pojo.vo.UploadFileVO;

import java.util.List;

/**
 * IFileFacade
 *
 * @author 王俊元（wangjunyuan@talkweb.com.cn）
 * @date 2023-05-17 11:13:49
 * @apiNote
 */
public interface IFileFacade {

    List<UploadFileVO> uploadFile(List<FIleBO> fileBOList);
}
