package net.petrikainulainen.spring.jooq.common.service;

import net.petrikainulainen.spring.jooq.common.TestDateUtil;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * @author Petri Kainulainen
 */
@Profile("test")
@Component
public class ConstantTimeDateTimeService implements DateTimeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConstantTimeDateTimeService.class);

    @Override
    public LocalDateTime getCurrentDateTime() {
        LOGGER.debug("Returning constant datetime: {}", TestDateUtil.CURRENT_TIMESTAMP);
        return TestDateUtil.parseLocalDateTime(TestDateUtil.CURRENT_TIMESTAMP);
    }

    @Override
    public Timestamp getCurrentTimestamp() {
        LOGGER.debug("Returning constant timestamp: {}", TestDateUtil.CURRENT_TIMESTAMP);
        return Timestamp.valueOf(TestDateUtil.CURRENT_TIMESTAMP);
    }
}
