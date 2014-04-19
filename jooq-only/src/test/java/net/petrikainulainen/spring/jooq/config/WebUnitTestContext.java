package net.petrikainulainen.spring.jooq.config;

import net.petrikainulainen.spring.jooq.todo.service.TodoCrudService;
import net.petrikainulainen.spring.jooq.todo.service.TodoSearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.mockito.Mockito.mock;

/**
 * @author Petri Kainulainen
 */
@Configuration
@Import({WebAppContext.class})
public class WebUnitTestContext {

    @Bean
    public TodoCrudService todoCrudService() {
        return mock(TodoCrudService.class);
    }

    @Bean
    public TodoSearchService todoSearchService() {
        return mock(TodoSearchService.class);
    }
}
