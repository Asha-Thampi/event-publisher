package com.messageBroker.eventpublisher.endpoint;

import com.messageBroker.eventpublisher.config.MessageSender;
import com.messageBroker.eventpublisher.model.EventModel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/event")
public class EventPublishController {

    @Autowired
    MessageSender messageSender;

    private final static Logger LOGGER = Logger.getLogger(EventPublishController.class.getName());

    /**
     * Controller for developer queue
     *
     * @param exchange
     * @param routingKey
     * @param eventModel
     */
    @PostMapping("/v1/publish/quorom/developer")
    public void publishToQuoromDeveloperQueue(@RequestParam("exchangeName") String exchange, @RequestParam("routingKey") String routingKey,  @RequestBody EventModel eventModel){
        LOGGER.info("Inside publish developer queue start");
        messageSender.sendToQuoromDeveloperQueue(exchange, routingKey, eventModel);
        LOGGER.info("Inside publish developer queue end");
    }

    /**
     * Controller for team queue
     *
     * @param exchange
     * @param eventModel
     */
    @PostMapping("/v1/publish/quorom/team")
    public void publishToQuoromTeamQueue(@RequestParam("exchangeName") String exchange,  @RequestBody EventModel eventModel){
        LOGGER.info("Inside publish team queue start");
        messageSender.sendToQuoromTeamQueue(exchange, eventModel);
        LOGGER.info("Inside publish team queue end");
    }

    /**
     * Controller for tester queue
     *
     * @param exchange
     * @param routingKey
     * @param eventModel
     */
    @PostMapping("/v1/publish/quorom/tester")
    public void publishToQuoromTesterQueue(@RequestParam("exchangeName") String exchange, @RequestParam("routingKey") String routingKey, @RequestBody EventModel eventModel){
        LOGGER.info("Inside publish tester queue start");
        messageSender.sendToQuoromTesterQueue(exchange, routingKey, eventModel);
        LOGGER.info("Inside publish tester queue end");
    }

    /**
     * Controller for manager queue
     *
     * @param exchange
     * @param department
     * @param message
     */
    @PostMapping("/v1/publish/quorom/manager")
    public void publishToQuoromManagerQueue(@RequestParam("exchangeName") String exchange, @RequestParam("department") String department, @RequestParam("messageData") String message){
        LOGGER.info("Inside publish manager queue start");
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("department", department);
        MessageConverter messageConverter = new SimpleMessageConverter();
        Message messageData = messageConverter.toMessage(message, messageProperties);
        messageSender.sendToQuoromManagerQueue(exchange, "", messageData);
        LOGGER.info("Inside publish manager queue end");
    }

    /**
     * Controller for stream queue
     *
     * @param exchange
     * @param department
     * @param message
     */
    @PostMapping("/v1/publish/stream")
    public void publishToStreamQueue(@RequestParam("exchangeName") String exchange, @RequestParam("department") String department, @RequestParam("messageData") String message){
        LOGGER.info("Inside publish stream queue start");
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("department", department);
        MessageConverter messageConverter = new SimpleMessageConverter();
        Message messageData = messageConverter.toMessage(message, messageProperties);
        messageSender.sendToStreamQueue(exchange, "", messageData);
        LOGGER.info("Inside publish stream queue end");
    }
}
