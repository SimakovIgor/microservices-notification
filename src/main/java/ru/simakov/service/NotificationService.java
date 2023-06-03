package ru.simakov.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.simakov.clients.notification.NotificationRequest;
import ru.simakov.repository.NotificationRepository;
import ru.simakov.model.entity.Notification;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    private static Notification mapNotification(NotificationRequest notificationRequest) {
        return Notification.builder()
                .toCustomerId(notificationRequest.getToCustomerId())
                .toCustomerEmail(notificationRequest.getToCustomerEmail())
                .message(notificationRequest.getMessage())
                .sender(notificationRequest.getSender())
                .build();
    }

    public void sendNotification(NotificationRequest notificationRequest) {
        var notification = mapNotification(notificationRequest);

        notificationRepository.save(notification);
    }
}
