package com.ruoyi.graphprocesser.common;


import com.ruoyi.common.config.MailConfig;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.MailUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;



@SpringBootTest
@DisplayName("单元测试案例")
public class NotificationServiceTest {

    @Autowired
    private RuoYiConfig ruoYiConfig;

    @Autowired
    private MailConfig mailConfig;



    @DisplayName("邮件通知测试")
    @Test
    public void testSendAlert() {
        // 调用 MailUtils.sendText 方法，发送测试告警邮件
        MailUtils.sendText("guosheng.zang@mail.nankai.edu.cn", "测试告警", "这是一条测试告警消息。");
        // 打印信息供参考
        System.out.println("测试告警邮件已发送，请检查邮箱！");
    }


    @DisplayName("单元测试1")
    @Test
    public void test1(){

        System.out.println("test1");
        System.out.println(ruoYiConfig);
    }
}