package ru.simakov.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import ru.simakov.clients.notification.NotificationRequest;
import ru.simakov.controller.support.IntegrationTestBase;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationControllerTest extends IntegrationTestBase {
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    void beforeEach() {
        restTemplateBuilder = new RestTemplateBuilder()
                .rootUri("http://localhost:" + localPort);
        testRestTemplate = new TestRestTemplate(restTemplateBuilder);
    }

    @SneakyThrows
    @Test
    void registerCustomer() {
        var notificationRequest = NotificationRequest.builder()
                .toCustomerEmail("email")
                .toCustomerId(1L)
                .message("message")
                .sender("sender")
                .build();
        testRestTemplate.postForEntity("/api/v1/notification",
                notificationRequest, Object.class);

        assertThat(notificationRepository.findAll())
                .first()
                .satisfies(fraudCheckHistory -> {
                    assertThat(fraudCheckHistory.getToCustomerEmail()).isEqualTo("email");
                    assertThat(fraudCheckHistory.getToCustomerId()).isEqualTo(1L);
                    assertThat(fraudCheckHistory.getMessage()).isEqualTo("message");
                    assertThat(fraudCheckHistory.getSender()).isEqualTo("sender");
                });
    }
}