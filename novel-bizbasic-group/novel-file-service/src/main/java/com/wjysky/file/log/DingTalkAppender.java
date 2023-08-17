package com.wjysky.file.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.filter.Filter;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.ListIterator;

/**
 * @author ：qyl
 * @description：logback 推送日志到钉钉
 * @date ：2022/4/8 10:25
 */
@Slf4j
@Data
@NoArgsConstructor
public class DingTalkAppender extends UnsynchronizedAppenderBase<LoggingEvent> {

    private String pattern;
    private PatternLayout layout;

    private String dingTalkApi = "https://oapi.dingtalk.com/robot/send?access_token=";
    private String dingTalkToken;
    private String dingTalkSecret;

    private RestTemplate restTemplate;


    @Override
    public void start() {
        if (StrUtil.isBlank(dingTalkToken) || StrUtil.isBlank(dingTalkSecret)) {
            addWarn("DingTalkAppender dingTalkToken or dingTalksecret is null ");
            System.out.println("dingTalkToken或dingTalksecret为空");
            return;
        }
        System.out.println("---------- 钉钉配置信息 S ----------");
        System.out.println("dingTalkToken：" + dingTalkToken);
        System.out.println("dingTalksecret：" + dingTalkSecret);
        System.out.println("---------- 钉钉配置信息 E ----------");
        OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory();
        requestFactory.setConnectTimeout(3 * 1000);
        requestFactory.setReadTimeout(5 * 1000);
        requestFactory.setWriteTimeout(5 * 1000);
        this.restTemplate = new RestTemplate(requestFactory);

        PatternLayout patternLayout = new PatternLayout();
        patternLayout.setContext(context);
        patternLayout.setPattern(pattern);
        patternLayout.start();
        this.layout = patternLayout;
        super.start();
    }

    @Override
    public void stop() {
        this.restTemplate = null;
        super.stop();
    }


    @Override
    protected void append(LoggingEvent loggingEvent) {
        if (!isStarted()){
            return;
        }
        if (loggingEvent.getLevel() != Level.ERROR) {
            return;
        }
        String toMsg = layout.doLayout(loggingEvent);
//        toDingDing(toMsg);
    }



    public void toDingDing( String contentMsg) {
        String url = dingTalkApi + dingTalkToken;

        String textMsg = "{\"msgtype\": \"text\",\"text\": {\"content\":\"" + contentMsg + "\"}}";
        
        try {

//        //加签
            if (dingTalkSecret != null) {
                Long timestamp = System.currentTimeMillis();
                String stringToSign = timestamp + "\n" + dingTalkSecret;
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(dingTalkSecret.getBytes("UTF-8"), "HmacSHA256"));
                byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
                String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
                url += "&timestamp=" + timestamp + "&sign=" + sign;
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity httpEntity = new HttpEntity(textMsg,headers);

            System.out.println("发送钉钉消息，请求地址：" + url + "，发送内容：" + textMsg);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
            System.out.println("发送钉钉日志响应：" + responseEntity.getBody());
        } catch (Exception e) {
            //防止重回 error日志队列导致阻塞
            e.printStackTrace();
            log.info("DingTalkAppender发送钉钉失败：{}", e);
        }
    }

}
