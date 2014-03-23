package net.petrikainulainen.spring.jooq.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * @author Petri Kainulainen
 */
@Configuration
@ComponentScan({
        "net.petrikainulainen.spring.jooq.todo.service"
})
@Import({
        PersistenceContext.class,
        WebAppContext.class
})
@ImportResource("classpath:org/jtransfo/spring/jTransfoContext.xml")
public class ExampleApplicationContext {
}
