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

    @Value("${spring.rabbitmq.queue.registrationQueue}")
    private String registrationQueue;

    @Value("${spring.rabbitmq.queue.resetPassword}")
    private String resetPasswordQueue;


    @Value("${spring.rabbitmq.template.exchange}")
    private String exchangeName;

    @Value("${spring.rabbitmq.template.routing-key.order.member}")
    private String routingKeyForMember;

    @Value("${spring.rabbitmq.template.routing-key.order.nonmember}")
    private String routingKeyForNonMember;

    @Value("${spring.rabbitmq.template.routing-key.order.shipped}")
    private String routingKeyForShipped;

    @Value("${spring.rabbitmq.template.routing-key.registration}")
    private String routingKeyForRegistration;

    @Value("${spring.rabbitmq.template.routing-key.reset-password}")
    private String routingKeyForResetPassword;


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
    public Queue registrationQueue() {
        return new Queue(registrationQueue, true);
    }

    @Bean
    public Queue resetPasswordQueue() {
        return new Queue(resetPasswordQueue, true);
    }

    @Bean
    public TopicExchange mailExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding orderMailBindingMember() {
        return BindingBuilder
                .bind(orderMailQueueMember())
                .to(mailExchange())
                .with(routingKeyForMember);
    }

    @Bean
    public Binding orderMailBindingNonMember() {
        return BindingBuilder
                .bind(orderMailQueueNonMember())
                .to(mailExchange())
                .with(routingKeyForNonMember);
    }

    @Bean
    public Binding orderShippedBinding() {
        return BindingBuilder
                .bind(orderShippedQueueMember())
                .to(mailExchange())
                .with(routingKeyForShipped);
    }

    @Bean
    public Binding registrationBinding() {
        return BindingBuilder
                .bind(registrationQueue())
                .to(mailExchange())
                .with(routingKeyForRegistration);
    }

    @Bean
    public Binding resetPasswordBinding() {
        return BindingBuilder
                .bind(resetPasswordQueue())
                .to(mailExchange())
                .with(routingKeyForResetPassword);
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
