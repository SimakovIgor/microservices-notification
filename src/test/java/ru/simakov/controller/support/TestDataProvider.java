package ru.simakov.controller.support;

import lombok.experimental.UtilityClass;
import ru.simakov.clients.notification.NotificationRequest;

@UtilityClass
public class TestDataProvider {
    public static NotificationRequest.NotificationRequestBuilder prepareNotificationRequest() {
        return NotificationRequest.builder()
                .toCustomerEmail("email")
                .toCustomerId(1L)
                .message("message")
                .sender("sender");
    }
}
