package com.wjysky.components.minio.config;

import com.wjysky.components.minio.utils.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MinioConfig
 *
 * @author 王俊元（wjysky520@gmail.com）
 * @date 2023-05-11 18:12:07
 * @apiNote minio存储服务配置类
 */

@Configuration
@EnableConfigurationProperties(MinioProperties.class)
@Slf4j
public class MinioConfig {

    @Bean(initMethod = "init")
    public MinioUtil minioUtil(MinioProperties properties) {
        return new MinioUtil(properties);
    }
}
