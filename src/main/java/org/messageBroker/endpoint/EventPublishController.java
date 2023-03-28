package org.messageBroker.endpoint;

import org.messageBroker.config.MessageSender;
import org.messageBroker.model.EventModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.logging.Logger;

@Controller
public class EventPublishController {

    @Autowired
    MessageSender messageSender;

    private final static Logger LOGGER = Logger.getLogger(EventPublishController.class.getName());

    @MessageMapping("/event.sendMessage")
    @SendTo("/topic/brokerMessage")
    public EventModel sendMessage(@Payload EventModel eventModel) {
        System.out.println("Event Name: " + eventModel.getName() + " and message is: " + eventModel.getMessage());
        return eventModel;
    }

    @MessageMapping("/event.newEvent")
    @SendTo("/topic/brokerMessage")
    public EventModel newUser(@Payload EventModel eventModel,
                                        SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", eventModel.getName());
        return eventModel;
    }
}