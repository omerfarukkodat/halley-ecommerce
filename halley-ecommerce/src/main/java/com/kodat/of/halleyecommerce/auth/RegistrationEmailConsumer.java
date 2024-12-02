package com.kodat.of.halleyecommerce.auth;

import com.kodat.of.halleyecommerce.dto.auth.RegistrationEmailDto;
import com.kodat.of.halleyecommerce.email.RegistrationEmailUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RegistrationEmailConsumer {

    private final RegistrationEmailUtils registrationEmailUtils;

    public RegistrationEmailConsumer(RegistrationEmailUtils registrationEmailUtils) {
        this.registrationEmailUtils = registrationEmailUtils;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.registrationQueue}")
    public void consumeRegistrationEmail(RegistrationEmailDto registrationEmailDto) {
        registrationEmailUtils.sendEmailForRegistration(registrationEmailDto);
    }

}
