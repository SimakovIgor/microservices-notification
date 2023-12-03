package ru.simakov.support;

import lombok.experimental.UtilityClass;
import ru.simakov.clients.notification.NotificationRequest;

@UtilityClass
@SuppressWarnings("PMD.TestClassWithoutTestCases")
public class TestDataProvider {
    public static NotificationRequest.NotificationRequestBuilder prepareNotificationRequest() {
        return NotificationRequest.builder()
            .toCustomerEmail("email")
            .toCustomerId(1L)
            .message("message")
            .sender("sender");
    }
}
