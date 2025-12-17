package com.ruoyi.graphprocesser.bizmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ProcessorMessageProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     * @param message
     */
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(MqConstant.PROCESSOR_EXCHANGE_NAME, MqConstant.PROCESSOR_ROUTING_KEY, message);
    }

}
