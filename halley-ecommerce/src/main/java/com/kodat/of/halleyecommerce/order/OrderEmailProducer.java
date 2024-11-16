package com.kodat.of.halleyecommerce.order;

import com.kodat.of.halleyecommerce.dto.order.EmailConsumerDto;
import com.kodat.of.halleyecommerce.user.GuestUser;
import com.kodat.of.halleyecommerce.user.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderEmailProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchangeName;

    @Value("${spring.rabbitmq.template.routing-key.member}")
    private String routingKeyMember;

    @Value("${spring.rabbitmq.template.routing-key.nonmember}")
    private String routingKeyNonmember;


    public OrderEmailProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderToQueueForMember(EmailConsumerDto emailConsumerDto) {
        rabbitTemplate.convertAndSend(exchangeName,routingKeyMember, emailConsumerDto);
    }
    @Transactional
    public void sendOrderToQueueNonMember(EmailConsumerDto emailConsumerDto) {
        rabbitTemplate.convertAndSend(exchangeName,routingKeyNonmember, emailConsumerDto);
    }


}
