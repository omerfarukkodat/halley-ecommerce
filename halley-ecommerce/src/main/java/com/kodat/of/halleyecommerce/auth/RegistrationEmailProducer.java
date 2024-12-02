package com.kodat.of.halleyecommerce.auth;

import com.kodat.of.halleyecommerce.dto.auth.RegistrationEmailDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RegistrationEmailProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.template.routing-key.registration}")
    private String routingKey;

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;

    public RegistrationEmailProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendRegistrationEmail(RegistrationEmailDto registrationEmailDto) {
        rabbitTemplate.convertAndSend(exchange,routingKey,registrationEmailDto);
    }


}
