package com.messageBroker.eventpublisher.config;

import com.messageBroker.eventpublisher.endpoint.EventPublishController;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.logging.Logger;

@Configuration
@EnableRabbit
public class SenderQueueConfig {

    @Value("${rabbit.quorom.team.queue.name}")
    private String quoromTeamQueue;
    
    @Value("${rabbit.quorom.developer.queue.name}")
    private String quoromDeveloperQueue;

    @Value("${rabbit.quorom.tester.queue.name}")
    private String quoromTesterQueue;

    @Value("${rabbit.quorom.manager.queue.name}")
    private String quoromManagerQueue;

    @Value("${rabbit.quorom.devops.queue.name}")
    private String quoromDevopsQueue;

    @Value("${rabbit.stream.queue.name}")
    private String streamQueue;

    @Value("${rabbit.quorom.queue.exchange}")
    private String quoromQueueExchange;

    @Value("${rabbit.quorom.team.queue.routingKey}")
    public String quoromTeamQueueRoutingKey;

    @Value("${rabbit.quorom.developer.queue.routingKey}")
    public String quoromDeveloperQueueRoutingKey;

    @Value("${rabbit.quorom.devops.queue.routingKey}")
    public String quoromDevopsQueueRoutingKey;

    private final static Logger LOGGER = Logger.getLogger(SenderQueueConfig.class.getName());

    /**
     *
     * Different types of exchanges
     *
     */

    //Direct exchange
    @Bean
    DirectExchange directExchange() {
        return new DirectExchange("direct-exchange");
    }


    //Fan-out exchange
    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanout-exchange");
    }

    // Topic exchange
    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange("topic-exchange");
    }

    // Headers exchange
    @Bean
    HeadersExchange headerExchange() {
        return new HeadersExchange("headers-exchange");
    }

    /**
     * Binding for team queue
     *
     * @param quoromTeamQueue
     * @param exchange
     * @return
     */
    @Bean
    Binding teamBinding(Queue quoromTeamQueue, FanoutExchange exchange) {
        return BindingBuilder.bind(quoromTeamQueue).to(exchange);
    }

    /**
     * Binding for developer queue
     *
     * @param quoromDeveloperQueue
     * @param exchange
     * @return
     */
    @Bean
    Binding developerBinding(Queue quoromDeveloperQueue, DirectExchange exchange) {
        return BindingBuilder.bind(quoromDeveloperQueue).to(exchange).with(quoromDeveloperQueueRoutingKey);
    }

    @Bean
    Binding devopsBinding(Queue quoromDevopsQueue, FanoutExchange exchange) {
        return BindingBuilder.bind(quoromDevopsQueue).to(exchange);
    }

    /**
     * Binding for tester queue
     *
     * @param quoromTesterQueue
     * @param exchange
     * @return
     */
    @Bean
    Binding testerBinding(Queue quoromTesterQueue, TopicExchange exchange) {
        return BindingBuilder.bind(quoromTesterQueue).to(exchange).with("quorom_*");
    }

    /**
     * Binding for tester queue
     *
     * @param quoromManagerQueue
     * @param exchange
     * @return
     */
    @Bean
    Binding managerBinding(Queue quoromManagerQueue, HeadersExchange exchange) {
        return BindingBuilder.bind(quoromManagerQueue).to(exchange).where("department").matches("manager");
    }

    @Bean
    Binding streamBinding(Queue streamQueue, HeadersExchange exchange) {
        return BindingBuilder.bind(streamQueue).to(exchange).where("department").matches("manager");
    }

    /**
     *
     * Queue Creation
     *
     */

    /**
     *
     * Team queue of type quorom
     *
     * @return queue
     */
    @Bean
    public Queue quoromTeamQueue() {
        return  new Queue(quoromTeamQueue, true, false, false, Map.of(
                "x-queue-type", "quorum"));
    }

    /**
     *
     * Developer queue of type quorom
     *
     * @return queue
     */
    @Bean
    public Queue quoromDeveloperQueue() {
        return  new Queue(quoromDeveloperQueue, true, false, false, Map.of(
                "x-queue-type", "quorum"));
    }

    @Bean
    public Queue quoromDevopsQueue() {
        return  new Queue(quoromDevopsQueue, true, false, false, Map.of(
                "x-queue-type", "quorum"));
    }

    /**
     *
     * Tester queue of type quorom
     *
     * @return queue
     */
    @Bean
    public Queue quoromTesterQueue() {
        return  new Queue(quoromTesterQueue, true, false, false, Map.of(
                "x-queue-type", "quorum"));
    }

    /**
     *
     * Manager queue of type quorom
     *
     * @return queue
     */
    @Bean
    public Queue quoromManagerQueue() {
        return  new Queue(quoromManagerQueue, true, false, false, Map.of(
                "x-queue-type", "quorum"));
    }

    /**
     *
     * Stream queue of type stream
     *
     * @return queue
     */
    @Bean
    public Queue streamQueue() {
        return new Queue(streamQueue, true, false, false, Map.of(
                "x-queue-type", "stream"));
    }

    /**
     * JSON Message converter bean
     *
     * @return converter
     */
    @Bean("Jackson2JsonMessageConverter")
    Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * helper class for sending and receiving messages
     *
     * @param connectionFactory
     * @param jackson2JsonMessageConverter
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
        return rabbitTemplate;
    }

}

