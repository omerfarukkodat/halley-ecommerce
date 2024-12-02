package com.kodat.of.halleyecommerce.user;

import com.kodat.of.halleyecommerce.dto.user.ResetPasswordEmailDto;
import com.kodat.of.halleyecommerce.email.UserResetPasswordUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class UserResetPasswordConsumer {
    private final UserResetPasswordUtils userResetPasswordUtils;

    public UserResetPasswordConsumer(UserResetPasswordUtils userResetPasswordUtils) {
        this.userResetPasswordUtils = userResetPasswordUtils;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.resetPassword}")
    public void consumeResetPasswordEmail(ResetPasswordEmailDto resetPasswordEmailDto){
        userResetPasswordUtils.sendEmailResetPassword(resetPasswordEmailDto);
    }
}
