package com.ruoyi.graphprocesser.bizmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ExperimentMessageProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     * @param message
     */
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(MqConstant.EXPERIMENT_EXCHANGE_NAME, MqConstant.EXPERIMENT_ROUTING_KEY, message);
        System.out.println("已发送消息 : " + message);
    }

}
