package com.wjysky.file.config.thread;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * FileController
 *
 * @author 王俊元（wjysky520@gmail.com）
 * @date 2023-05-17 09:09:43
 * @apiNote 批量上传文件线程池
 */
@EnableAsync
@Configuration
@Slf4j
@Data
@ConfigurationProperties(prefix = "thread.pool.upload-file")
public class UploadFileConfig implements AsyncConfigurer {

    /**
     * 核心线程数，IO密集型
     */
    private int corePoolSize;

    /**
     * 最大工作线程数
     */
    private int maxPoolSize;

    /**
     * 允许线程空闲时间（单位为秒）
     */
    private int keepAliveTime;

    /**
     * 缓冲队列数
     */
    private int queueCapacity;

    /**
     * 线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁
     */
    private int awaitTermination;

    /**
     * 拒绝策略
     */
    private String rejectedExecutionHandler;

    /**
     * 用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
     */
    private Boolean waitCompleteOnShutdown;

    /**
     * 线程池名前缀
     */
    private String threadNamePrefix;

    @Bean("uploadFileTaskExecutor")
    public ThreadPoolTaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize > 0 ? corePoolSize : Runtime.getRuntime().availableProcessors() * 2);
        executor.setMaxPoolSize(maxPoolSize > 0 ? maxPoolSize : Runtime.getRuntime().availableProcessors() * 5);
        executor.setKeepAliveSeconds(keepAliveTime > 0 ? keepAliveTime : 10);
        executor.setQueueCapacity(queueCapacity > 0 ? queueCapacity : Runtime.getRuntime().availableProcessors() * 2);
        executor.setThreadNamePrefix(StringUtils.isNotBlank(threadNamePrefix) ? threadNamePrefix : "Async-Upload-File-");
        executor.setWaitForTasksToCompleteOnShutdown(waitCompleteOnShutdown != null ? waitCompleteOnShutdown : true);
        executor.setAwaitTerminationSeconds(awaitTermination > 0 ? awaitTermination : 60);
        try {
            // 反射加载拒绝策略类
            Class<?> clazz = Class.forName("java.util.concurrent.ThreadPoolExecutor$" + this.rejectedExecutionHandler);
            executor.setRejectedExecutionHandler((RejectedExecutionHandler) clazz.newInstance());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            log.error("反射加载拒绝策略类时异常", e);
            // 默认使用CallerRunsPolicy策略：直接在execute方法的调用线程中运行被拒绝的任务
            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        }
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (Throwable ex, Method method, Object... params) -> {
            log.error("class#method: " + method.getDeclaringClass().getName() + "#" + method.getName());
            log.error("type        : " + ex.getClass().getName());
            log.error("exception   : " + ex.getMessage());
            log.error("error-info  : ", ex);
        };
    }
}
