package ru.simakov.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.simakov.clients.notification.NotificationRequest;
import ru.simakov.service.NotificationService;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
    private final NotificationService notificationService;

    @RabbitListener(queues = "${rabbitmq.queue.notification}")
    public void consumer(final NotificationRequest notificationRequest) {
        log.info("Consumed message: {}", notificationRequest);
        notificationService.sendNotification(notificationRequest);
    }
}
