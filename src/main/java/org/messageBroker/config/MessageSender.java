package org.messageBroker.config;

import org.messageBroker.model.EventModel;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SimpMessagingTemplate template;

    public void sendToStreamTesterQueue(String exchange, String routingKey, String eventModel ) {
        try{
            rabbitTemplate.convertAndSend(exchange, routingKey, eventModel);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     * Sends the message to Stream Admin Queue
     *
     * @param eventModel
     */
    public void sendToStreamAdminQueue(String exchange, String routingKey, EventModel eventModel) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, eventModel, message -> {
                    MessageProperties properties = message.getMessageProperties();
                    properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return new org.springframework.amqp.core.Message(message.getBody(), properties);

            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}