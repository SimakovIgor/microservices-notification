package ru.simakov.controller.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.simakov.repository.NotificationRepository;
import ru.simakov.starter.testing.base.DatabaseAwareTestBase;
import ru.simakov.starter.testing.initializer.PostgreSQLInitializer;
import ru.simakov.starter.testing.initializer.RabbitMQInitializer;

import java.util.Set;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@ContextConfiguration(initializers = {
        PostgreSQLInitializer.class,
        RabbitMQInitializer.class
})
public abstract class IntegrationTestBase extends DatabaseAwareTestBase {
    @Autowired
    protected NotificationRepository notificationRepository;
    @LocalServerPort
    protected int localPort;

    @Override
    protected String getSchema() {
        return "public";
    }

    @Override
    protected Set<String> getTables() {
        return Set.of("notification");
    }

}
