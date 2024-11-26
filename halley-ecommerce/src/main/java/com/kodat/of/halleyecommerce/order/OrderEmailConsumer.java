package com.kodat.of.halleyecommerce.order;

import com.kodat.of.halleyecommerce.dto.order.EmailConsumerDto;
import com.kodat.of.halleyecommerce.util.OrderEmailUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEmailConsumer {
    private final OrderEmailUtils orderEmailUtils;

    public OrderEmailConsumer(OrderEmailUtils orderEmailUtils) {
        this.orderEmailUtils = orderEmailUtils;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.orderMailQueueMember}")
    public void consumeOrderEmailForMember (EmailConsumerDto emailConsumerDto) {
        orderEmailUtils.sendEmailForOrderSummary(emailConsumerDto);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.orderMailQueueNonMember}")
    public void consumeOrderEmailForNonMember(EmailConsumerDto emailConsumerDto){
        orderEmailUtils.sendEmailForOrderSummary(emailConsumerDto);
    }
    @RabbitListener(queues = "${spring.rabbitmq.queue.orderShippedQueue}")
    public void consumeOrderShippedQueue(EmailConsumerDto emailConsumerDto){
        orderEmailUtils.sendEmailForOrderShipped(emailConsumerDto);
    }

}
