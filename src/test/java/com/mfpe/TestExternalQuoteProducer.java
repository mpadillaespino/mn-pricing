package com.mfpe;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.core.util.StringUtils;
import org.awaitility.Awaitility;
import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class TestExternalQuoteProducer {

    private static final Logger LOG = LoggerFactory.getLogger(TestExternalQuoteProducer.class);
    private static final String PROPERTY_NAME = "TestExternalQuoteProducer";

    @Rule
    public static KafkaContainer kafkaContainer = new KafkaContainer();

    private static ApplicationContext context;

    @BeforeAll
    static void startKafka(){
        kafkaContainer.start();
        LOG.debug("Bootstrap Servers: {}", kafkaContainer.getBootstrapServers());

        context = ApplicationContext.run(
                CollectionUtils.mapOf(
                        "kafka.bootstrap.servers", kafkaContainer.getBootstrapServers(),
                        PROPERTY_NAME, StringUtils.TRUE
                ),
                Environment.TEST
        );
    }

    @AfterAll
    static void stopKafka(){
        kafkaContainer.stop();
        context.close();
    }

    @Test
    void producing10RecordsWorks() {
        ExternalQuoteProducer producer = context.getBean(ExternalQuoteProducer.class);
        ExternalQuoteObserver observer = context.getBean(ExternalQuoteObserver.class);

        IntStream.range(0,10).forEach(count -> {
            producer.send("TEST-" + count, new ExternalQuote(
                    "TEST-" + count,
                    randomValue(),
                    randomValue()
            ));
        });

        Awaitility.await().untilAsserted(() -> {
            assertEquals(10, observer.inspected.size());
        });
    }

    private BigDecimal randomValue() {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(0, 1000));
    }

    // Para verificar que el producer este funcionando creo un Observer/Consumer.
    @KafkaListener(
            offsetReset = OffsetReset.EARLIEST
    )
    @Requires(env = Environment.TEST)
    @Requires(property = PROPERTY_NAME, value = StringUtils.TRUE)
    static class ExternalQuoteObserver {

        List<ExternalQuote> inspected = new ArrayList<>();

        @Topic("external-quotes")
        void recieve(ExternalQuote quote){
            LOG.info("Consumed {}", quote);
            inspected.add(quote);
        }

    }

}
