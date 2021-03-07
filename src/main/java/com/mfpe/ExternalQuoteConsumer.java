package com.mfpe;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@KafkaListener(
        clientId = "mn-pricing-external-quote-consumer",
        groupId = "external-quote-consumer",
        batch = true
)
public class ExternalQuoteConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalQuoteConsumer.class);

    @Topic("external-quotes")
    void recieve(List<ExternalQuote> externalQuoteList){
        LOG.info("Consuming batch of external quotes {}", externalQuoteList);
    }

}
