package org.jeecg.modules.rabbitmq.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author tkx
 * @Date 2024 11 25 23 40
 **/
@Configuration
public class RabbitConfig {


    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        // 设置开启Mandatory,才能触发回调函数。
        // 无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("ConfirmCallBack   "+"相关数据: "+ correlationData);
                System.out.println("ConfirmCallBack   "+"确认情况: "+ ack);
                System.out.println("ConfirmCallBack   "+"原因: "+ cause);
            }
        });

        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println("ReturnCallBack:   " +"消息 "+message);
                System.out.println("ReturnCallBack:   " +"回应码 "+replyCode);
                System.out.println("ReturnCallBack:   " +"回应消息 "+replyText);
                System.out.println("ReturnCallBack:   " +"交换机 "+exchange);
                System.out.println("ReturnCallBack:   " +"路由键 "+routingKey);
            }
        });
        return rabbitTemplate;
    }
}
