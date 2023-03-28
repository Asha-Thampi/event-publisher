package org.messageBroker.config;

import com.messageBroker.eventpublisher.model.EventModel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     *
     * Sends the message to the developer queue
     *
     * @param exchange
     * @param routingKey
     * @param eventModel
     */
    public void sendToQuoromDeveloperQueue(String exchange, String routingKey, EventModel eventModel) {
        try{
            rabbitTemplate.convertAndSend(exchange, routingKey, eventModel);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Sends the message to the team queue
     *
     * @param exchange the type of exchange
//     * @param message the message to be sent
     *
     */
    public void sendToQuoromTeamQueue(String exchange, EventModel eventModel) {
        try{
            rabbitTemplate.convertAndSend(exchange, "", eventModel);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Sends the message to the tester queue
     *
     * @param exchange the type of exchange
     * @param eventModel the message to be sent
     *
     */
    public void sendToQuoromTesterQueue(String exchange, String routingKey, EventModel eventModel) {
        try{
            rabbitTemplate.convertAndSend(exchange, routingKey, eventModel);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Sends the message to the tester queue
     *
     * @param exchange the type of exchange
     * @param message the message to be sent
     *
     */
    public void sendToQuoromManagerQueue(String exchange, String routingKey, Message message) {
        try{
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     *
     * Sends the message to Stream Queue
     *
     * @param message
     */
    public void sendToStreamQueue(String exchange, String routingKey, Message message) {
        try{
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}