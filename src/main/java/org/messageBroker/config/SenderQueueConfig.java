package org.messageBroker.config;

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

    @Value("${rabbit.stream.queue.name}")
    private String streamQueue;

    @Value("${rabbit.stream.developer.queue.name}")
    private String streamDeveloperQueue;

    @Value("${rabbit.stream.team.queue.name}")
    private String streamTeamQueue;

    @Value("${rabbit.stream.tester.queue.name}")
    private String streamTesterQueue;

    @Value("${rabbit.stream.manager.queue.name}")
    private String streamManagerQueue;

    @Value("${rabbit.stream.admin.queue.name}")
    private String streamAdminQueue;

    @Value("${rabbit.quorom.developer.queue.routingKey}")
    public String quoromDeveloperQueueRoutingKey;

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


    @Bean
    Binding streamBinding(Queue streamQueue, HeadersExchange exchange) {
        return BindingBuilder.bind(streamQueue).to(exchange).where("department").matches("manager");
    }

    /**
     * Binding the developer queue with the routing key
     *
     * @param streamDeveloperQueue
     * @param exchange
     * @return binding
     *
     */
    @Bean
    Binding streamDeveloperBinding(Queue streamDeveloperQueue, DirectExchange exchange) {
        return BindingBuilder.bind(streamDeveloperQueue).to(exchange).with(quoromDeveloperQueueRoutingKey);
    }

    /**
     * Binding the team queue with the fanout exchange
     *
     * @param streamTeamQueue
     * @param exchange
     * @return binding
     *
     */
    @Bean
    Binding streamTeamBinding(Queue streamTeamQueue, FanoutExchange exchange) {
        return BindingBuilder.bind(streamTeamQueue).to(exchange);
    }

    /**
     * Binding tester queue with
     * @param streamTesterQueue
     * @param exchange
     * @return
     */
    @Bean
    Binding streamTesterBinding(Queue streamTesterQueue, TopicExchange exchange) {
        return BindingBuilder.bind(streamTesterQueue).to(exchange).with("quorom_*");
    }

    @Bean
    Binding streamManagerBinding(Queue streamManagerQueue, HeadersExchange exchange) {
        return BindingBuilder.bind(streamManagerQueue).to(exchange).where("department").matches("manager");
    }

    @Bean
    Binding streamAdminBinding(Queue streamAdminQueue, TopicExchange exchange) {
        return BindingBuilder.bind(streamAdminQueue).to(exchange).with("quorom_*");
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

    @Bean
    public Queue streamDeveloperQueue() {
        return new Queue(streamDeveloperQueue, true, false, false, Map.of(
                "x-queue-type", "stream"));
    }

    @Bean
    public Queue streamTeamQueue() {
        return new Queue(streamTeamQueue, true, false, false, Map.of(
                "x-queue-type", "stream"));
    }

    @Bean
    public Queue streamTesterQueue() {
        return new Queue(streamTesterQueue, true, false, false, Map.of(
                "x-queue-type", "stream"));
    }

    @Bean
    public Queue streamManagerQueue() {
        return new Queue(streamManagerQueue, true, false, false, Map.of(
                "x-queue-type", "stream"));
    }

    @Bean
    public Queue streamAdminQueue() {
        return new Queue(streamAdminQueue, true, false, false, Map.of(
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

