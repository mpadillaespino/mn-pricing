package com.mfpe;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Singleton
@Requires(notEnv = Environment.TEST)
public class EventScheduler {

    private static final List<String> SYMBOLS = Arrays.asList("APPlE","AMAZON", "GOOGLE","FACEBOOK","NETFLIX");
    private static final Logger LOG = LoggerFactory.getLogger(EventScheduler.class);
    private final ExternalQuoteProducer externalQuoteProducer;

    public EventScheduler(ExternalQuoteProducer externalQuoteProducer) {
        this.externalQuoteProducer = externalQuoteProducer;
    }

    @Scheduled(fixedDelay = "10s")
    void generate(){
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        ExternalQuote externalQuote = new ExternalQuote(
                SYMBOLS.get(random.nextInt(0, SYMBOLS.size() - 1)),
                randomValue(random),
                randomValue(random)
        );
        externalQuoteProducer.send(externalQuote.getSymbol(), externalQuote);
        LOG.info("Generate external quote {}", externalQuote);
    }

    private BigDecimal randomValue(ThreadLocalRandom random) {
        return BigDecimal.valueOf(random.nextDouble(0, 1000));
    }

}