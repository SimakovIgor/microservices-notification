package ru.simakov.contract;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierSender;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@TestConfiguration
@SuppressWarnings("checkstyle:anoninnerlength")
class MessageVerifierSenderConfig {

    @Bean
    static MessageVerifierSender<Message<?>> testMessageVerifier(final RabbitTemplate rabbitTemplate) {
        return new MessageVerifierSender<>() {

            @Override
            public void send(final Message<?> message,
                             final String destination,
                             final YamlContract contract) {
                log.info("Sending a message to destination [{}]", destination);
                org.springframework.amqp.core.Message convertedMessage = toMessage(message);
                String receivedRoutingKey = convertedMessage.getMessageProperties().getReceivedRoutingKey();
                rabbitTemplate.send(destination, receivedRoutingKey, convertedMessage);
            }

            @Override
            public <T> void send(final T payload,
                                 final Map<String, Object> headers,
                                 final String destination,
                                 final YamlContract contract) {
                send(org.springframework.messaging.support.MessageBuilder.withPayload(payload)
                    .copyHeaders(headers).build(), destination, contract);
            }

            private org.springframework.amqp.core.Message toMessage(final Message<?> msg) {
                log.info("toMessage [{}]", msg);

                final Map<String, Object> newHeaders = new ConcurrentHashMap<>(msg.getHeaders());
                final MessageProperties messageProperties = new MessageProperties();
                newHeaders.forEach(messageProperties::setHeader);

                if (msg.getPayload() instanceof String json) {
                    return MessageBuilder.withBody(json.getBytes(StandardCharsets.UTF_8))
                        .andProperties(messageProperties)
                        .build();
                }

                throw new IllegalStateException("Payload is not a String");
            }
        };

    }
}
