package org.messageBroker.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.logging.Logger;

@Configuration
@EnableRabbit
public class SenderQueueConfig {
//
//    @Value("${rabbit.stream.queue.name}")
//    private String streamQueue;
//
//    @Value("${rabbit.stream.developer.queue.name}")
//    private String streamDeveloperQueue;
//
//    @Value("${rabbit.stream.devops.queue.name}")
//    private String streamDevopsQueue;
//
    @Value("${rabbit.stream.team.queue.name}")
    private String streamTeamQueue;

    @Value("${rabbit.stream.tester.queue.name}")
    private String streamTesterQueue;
//
//    @Value("${rabbit.stream.manager.queue.name}")
//    private String streamManagerQueue;
//
//    @Value("${rabbit.stream.admin.queue.name}")
//    private String streamAdminQueue;
//
//    @Value("${rabbit.stream.developer.queue.routingKey}")
//    public String streamDeveloperQueueRoutingKey;
//
//    @Value("${rabbit.quorom.devops.queue.routingKey}")
//    public String quoromDevopsQueueRoutingKey;
//
    @Autowired
    private AmqpAdmin amqpAdmin;

    private final static Logger LOGGER = Logger.getLogger(SenderQueueConfig.class.getName());

//    /**
//     *
//     * Different types of exchanges
//     *
//     */
//
//    //Direct exchange
//    @Bean
//    DirectExchange directExchange() {
//        return new DirectExchange("direct-exchange");
//    }
//
//
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
//
//    // Headers exchange
//    @Bean
//    HeadersExchange headerExchange() {
//        return new HeadersExchange("headers-exchange");
//    }
//
//
//    /**
//     * Declare Queues
//     **/
    @Bean
    Queue streamTeamQueue() {
        return new Queue(streamTeamQueue, true, false, false, Map.of(
                "x-queue-type", "stream"));
    }

    @Bean
    Queue streamTesterQueue() {
        return new Queue(streamTesterQueue, true, false, false, Map.of(
                "x-queue-type", "stream"));
    }

//    @Bean
//    Queue streamAdminQueue() {
//        return new Queue(streamAdminQueue, true, false, false, Map.of(
//                "x-queue-type", "stream"));
//    }
//
    @Bean
    Binding TeamBinding(Queue streamTeamQueue, FanoutExchange exchange) {
        amqpAdmin.declareQueue(streamTeamQueue);
        return BindingBuilder.bind(streamTeamQueue).to(exchange);
    }

    @Bean
    Binding TesterBinding(Queue streamTesterQueue, TopicExchange exchange) {
        amqpAdmin.declareQueue(streamTesterQueue);
        return BindingBuilder.bind(streamTesterQueue).to(exchange).with("/topic/public");
    }
//
//    @Bean
//    Binding AdminBinding(Queue streamAdminQueue, TopicExchange exchange) {
//        amqpAdmin.declareQueue(streamAdminQueue);
//        return BindingBuilder.bind(streamAdminQueue).to(exchange).with("new_");
//    }
//
//    /**
//     *
//     * Stream queue of type stream
//     *
//     * @return queue
//     */
////    @Bean
////    public Queue streamQueue() {
////        return new Queue(streamQueue, true, false, false, Map.of(
////                "x-queue-type", "stream"));
////    }
//
////    @Bean
////    public Queue streamDeveloperQueue() {
////        return new Queue(streamDeveloperQueue, true, false, false, Map.of(
////                "x-queue-type", "stream"));
////    }
////
////    @Bean
////    public Queue streamDevopsQueue() {
////        return new Queue(streamDevopsQueue, true, false, false, Map.of(
////                "x-queue-type", "stream", "delivery_mode","2"));
////    }
//
//
//
//
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