package com.ruoyi.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import cn.hutool.extra.mail.MailAccount;

@Configuration
public class MailConfig {

    @Value("${mail.host}")
    private String host;

    @Value("${mail.port}")
    private Integer port;

    @Value("${mail.auth}")
    private Boolean auth;

    @Value("${mail.from}")
    private String from;

    @Value("${mail.user}")
    private String user;

    @Value("${mail.pass}")
    private String pass;

    @Value("${mail.starttlsEnable}")
    private Boolean starttlsEnable;

    @Value("${mail.sslEnable}")
    private Boolean sslEnable;

    @Value("${mail.timeout}")
    private Integer timeout;

    @Value("${mail.connectionTimeout}")
    private Integer connectionTimeout;

    @Bean
    @ConditionalOnProperty(value = "mail.enabled", havingValue = "true")
    public MailAccount mailAccount() {
        MailAccount account = new MailAccount();
        account.setHost(host);
        account.setPort(port);
        account.setAuth(auth);
        account.setFrom(from);
        account.setUser(user);
        account.setPass(pass);
        account.setSocketFactoryPort(port);
        account.setStarttlsEnable(starttlsEnable);
        account.setSslEnable(sslEnable);
        account.setTimeout(timeout);
        account.setConnectionTimeout(connectionTimeout);
        return account;
    }
}
