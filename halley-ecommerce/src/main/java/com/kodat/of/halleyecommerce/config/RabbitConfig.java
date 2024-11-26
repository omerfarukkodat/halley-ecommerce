package com.kodat.of.halleyecommerce.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${spring.rabbitmq.queue.orderMailQueueMember}")
    private String orderMailQueueMember;

    @Value("${spring.rabbitmq.queue.orderMailQueueNonMember}")
    private String orderMailQueueNonMember;

    @Value("${spring.rabbitmq.queue.orderShippedQueue}")
    private String orderShippedQueue;


    @Value("${spring.rabbitmq.template.exchange}")
    private String exchangeName;

    @Value("${spring.rabbitmq.template.routing-key.member}")
    private String routingKeyForMember;

    @Value("${spring.rabbitmq.template.routing-key.nonmember}")
    private String routingKeyForNonMember;

    @Value("${spring.rabbitmq.template.routing-key.shipped}")
    private String routingKeyForShipped;



    @Bean
    public Queue orderMailQueueMember() {
        return new Queue(orderMailQueueMember, true);
    }

    @Bean
    public Queue orderMailQueueNonMember() {
        return new Queue(orderMailQueueNonMember, true);
    }

    @Bean
    public Queue orderShippedQueueMember() {
        return new Queue(orderShippedQueue, true);
    }


    @Bean
    public TopicExchange orderMailExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding orderMailBindingMember() {
        return BindingBuilder
                .bind(orderMailQueueMember())
                .to(orderMailExchange())
                .with(routingKeyForMember);
    }

    @Bean
    public Binding orderMailBindingNonMember() {
        return BindingBuilder
                .bind(orderMailQueueNonMember())
                .to(orderMailExchange())
                .with(routingKeyForNonMember);
    }

    @Bean
    public Binding orderShippedBinding() {
        return BindingBuilder
                .bind(orderShippedQueueMember())
                .to(orderMailExchange())
                .with(routingKeyForShipped);
    }


    @Bean
    public MessageConverter jacksonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }


}
