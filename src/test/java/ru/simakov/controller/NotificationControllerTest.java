package ru.simakov.controller;

import org.junit.jupiter.api.Test;
import ru.simakov.clients.notification.NotificationRequest;
import ru.simakov.model.entity.Notification;
import ru.simakov.support.IntegrationTestBase;
import ru.simakov.support.TestDataProvider;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationControllerTest extends IntegrationTestBase {

    @Test
    void registerCustomer() {
        var notificationRequest = TestDataProvider.prepareNotificationRequest().build();
        postNotification(notificationRequest);

        assertThat(notificationRepository.findAll())
            .first()
            .extracting(Notification::getToCustomerEmail, Notification::getToCustomerId,
                Notification::getMessage, Notification::getSender)
            .containsExactly("email", 1L, "message", "sender");
    }

    private void postNotification(final NotificationRequest request) {
        webTestClient.post()
            .uri(uriBuilder -> uriBuilder
                .host("localhost")
                .port(localPort)
                .pathSegment("api", "v1", "notification")
                .build())
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk();
    }

}
