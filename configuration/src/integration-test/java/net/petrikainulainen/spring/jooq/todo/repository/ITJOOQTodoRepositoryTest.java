package net.petrikainulainen.spring.jooq.todo.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import net.petrikainulainen.spring.jooq.config.PersistenceContext;
import net.petrikainulainen.spring.jooq.todo.exception.NotFoundException;
import net.petrikainulainen.spring.jooq.todo.model.Todo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petri Kainulainen
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceContext.class})
//@ContextConfiguration(locations = {"classpath:exampleApplicationContext-persistence.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
public class ITJOOQTodoRepositoryTest {

    @Autowired
    private TodoRepository repository;

    @Test
    @DatabaseSetup("todo-data.xml")
    public void findById_TodoFound_ShouldReturnTodo() {
        Todo found = repository.findById(1L);

        assertThat(found.getId()).isEqualTo(1L);
        assertThat(found.getDescription()).isEqualTo("Lorem ipsum");
        assertThat(found.getTitle()).isEqualTo("Foo");
    }

    @Test
    @DatabaseSetup("todo-data.xml")
    public void findById_TodoNotFound_ShouldThrowException() {
        catchException(repository).findById(999L);
        assertThat(caughtException()).isExactlyInstanceOf(NotFoundException.class);
    }

    @Test
    @DatabaseSetup("todo-data.xml")
    @ExpectedDatabase("todo-data-updated.xml")
    public void update_TodoFound_ShouldUpdateTodo() {
        Todo updated = Todo.getBuilder("title")
                .description("description")
                .id(2L)
                .build();

        repository.update(updated);
    }

    @Test
    @DatabaseSetup("todo-data.xml")
    @ExpectedDatabase("todo-data.xml")
    public void updateAndThrowException_TodoFound_ShouldThrowExceptionAndRollbackTransaction() {
        Todo updated = Todo.getBuilder("title")
                .description("description")
                .id(2L)
                .build();

        catchException(repository).updateAndThrowException(updated);
        assertThat(caughtException())
                .isExactlyInstanceOf(BadSqlGrammarException.class);
    }
}
