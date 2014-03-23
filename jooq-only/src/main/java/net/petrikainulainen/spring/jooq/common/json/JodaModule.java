package net.petrikainulainen.spring.jooq.common.json;

import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.joda.time.LocalDateTime;

/**
 * @author Petri Kainulainen
 */
public class JodaModule extends SimpleModule {

    public JodaModule() {
        super(PackageVersion.VERSION);

        addDeserializer(LocalDateTime.class, new CustomLocalDateTimeDeserializer());
        addSerializer(LocalDateTime.class, new CustomLocalDateTimeSerializer());
    }
}
