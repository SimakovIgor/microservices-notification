package ru.simakov.contract;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import ru.simakov.Application;
import ru.simakov.service.NotificationService;
import ru.simakov.starter.testing.initializer.PostgreSQLInitializer;
import ru.simakov.starter.testing.initializer.RabbitMQInitializer;

import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@SpringBootTest(
    classes = {
        Application.class,
        MessageVerifierSenderConfig.class
    },
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = "stubrunner.amqp.mockConnection=false"
)
@ContextConfiguration(initializers = {
    PostgreSQLInitializer.class,
    RabbitMQInitializer.class
})
@AutoConfigureStubRunner(
    ids = "ru.simakov.microservices:microservices-customer",
    stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
class ContractRabbitMqTest {

    @MockBean
    private NotificationService notificationService;
    @Autowired
    private StubTrigger stubTrigger;

    @Test
    void shouldReceiveNotification() {
        stubTrigger.trigger("emitPublishCustomerNotificationEvent");

        Awaitility.await().untilAsserted(() ->
            Mockito.verify(notificationService).sendNotification(any()));

    }
}

