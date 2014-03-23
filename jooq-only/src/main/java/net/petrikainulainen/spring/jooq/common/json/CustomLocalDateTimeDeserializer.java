package net.petrikainulainen.spring.jooq.common.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import net.petrikainulainen.spring.jooq.common.DateTimeConstants;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

/**
 * @author Petri Kainulainen
 */
public class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DateTimeConstants.TIMESTAMP_PATTERN);

    public CustomLocalDateTimeDeserializer() {
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        String dateTimeString = jsonParser.getText();

        if (dateTimeString.isEmpty()) {
            return null;
        }

        return dateTimeFormatter.parseLocalDateTime(dateTimeString);
    }
}
