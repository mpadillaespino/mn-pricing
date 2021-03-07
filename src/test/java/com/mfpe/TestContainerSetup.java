package com.mfpe;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class TestContainerSetup {

    private static final Logger LOG = LoggerFactory.getLogger(TestContainerSetup.class);

    @Rule
    public KafkaContainer kafkaContainer = new KafkaContainer();

    @Test
    void testItWorks() {
        kafkaContainer.start();
        LOG.info("Boostrap servers: {}", kafkaContainer.getBootstrapServers());
        kafkaContainer.stop();
    }

}
