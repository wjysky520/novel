package com.wjysky.file.task;

import com.wjysky.file.strategy.FileStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pojo.bo.FileBO;

import java.util.concurrent.CompletableFuture;

/**
 * @author : 王俊元(wjysky520@gmail.com)
 * @ClassName : UploadFileTask
 * @Description : TODO
 * @Date : 2023-08-17 11:27:03
 */
@Component
@Slf4j
public class UploadFileTask {

    @Async("uploadFileTaskExecutor")
    public CompletableFuture<String> uploadFile(FileStrategy fileStrategy, FileBO fileBO) {
        return CompletableFuture.supplyAsync(() -> fileStrategy.uploadFile(fileBO));
    }
}