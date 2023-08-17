package com.wjysky.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
@ComponentScan({"com.wjysky.**", "com.wjysky.*.*.config"})
public class NovelFileServiceApplication {

    public static void main(String[] args) {
        ConfigurableEnvironment env = SpringApplication.run(NovelFileServiceApplication.class, args).getEnvironment();
        String serviceName = env.getProperty("spring.application.name"); // 服务名称
        String servicePort = env.getProperty("server.port"); // 服务端口
        String[] serviceActive = env.getActiveProfiles(); // 运行环境
        log.info("\n------------------------------------------------------------\n" +
                        "\n\t[{}]启动完毕，端口：[{}]，运行环境为：[{}]\n" +
                        "\n------------------------------------------------------------",
                serviceName, servicePort, serviceActive);
    }

}
