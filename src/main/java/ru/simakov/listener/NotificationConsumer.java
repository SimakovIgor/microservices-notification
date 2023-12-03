package ru.simakov.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.simakov.clients.notification.NotificationRequest;
import ru.simakov.service.NotificationService;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
    private final NotificationService notificationService;

    @RabbitListener(
        id = "listenNotificationRequest",
        bindings = @QueueBinding(
            value = @Queue(value = "internal.exchange.queue}", durable = "true"),
            exchange = @Exchange(value = "internal.exchange", type = ExchangeTypes.HEADERS),
            arguments = @Argument(name = "amqp_receivedRoutingKey", value = "#")
        )
    )
    public void listenNotificationRequest(final NotificationRequest event) {
        log.info("NotificationRequest Received: " + event);
        notificationService.sendNotification(event);
    }
}
