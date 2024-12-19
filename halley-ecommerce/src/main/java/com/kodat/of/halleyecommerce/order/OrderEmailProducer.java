package com.kodat.of.halleyecommerce.order;

import com.kodat.of.halleyecommerce.dto.order.EmailConsumerDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderEmailProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchangeName;

    @Value("${spring.rabbitmq.template.routing-key.order.member}")
    private String routingKeyMember;

    @Value("${spring.rabbitmq.template.routing-key.order.nonmember}")
    private String routingKeyNonmember;

    @Value("${spring.rabbitmq.template.routing-key.order.shipped}")
    private String routingKeyShipped;


    public OrderEmailProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderToQueueForMember(EmailConsumerDto emailConsumerDto) {
        rabbitTemplate.convertAndSend(exchangeName,routingKeyMember, emailConsumerDto);
    }

    public void sendOrderToQueueNonMember(EmailConsumerDto emailConsumerDto) {
        rabbitTemplate.convertAndSend(exchangeName,routingKeyNonmember, emailConsumerDto);
    }

    public void sendOrderShippedQueue(EmailConsumerDto emailConsumerDto) {
        rabbitTemplate.convertAndSend(exchangeName,routingKeyShipped, emailConsumerDto);
    }
}
