package com.kodat.of.halleyecommerce.user;

import com.kodat.of.halleyecommerce.dto.user.ResetPasswordEmailDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserResetPasswordProducer {
    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchangeName;

    @Value("${spring.rabbitmq.template.routing-key.reset-password}")
    private String resetPasswordRoutingKey;

    public UserResetPasswordProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendResetPasswordQueue(ResetPasswordEmailDto resetPasswordEmailDto) {
        rabbitTemplate.convertAndSend(exchangeName, resetPasswordRoutingKey, resetPasswordEmailDto);
    }
}
