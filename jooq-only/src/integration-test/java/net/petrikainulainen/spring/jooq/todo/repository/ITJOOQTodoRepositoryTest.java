package net.petrikainulainen.spring.jooq.todo.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import net.petrikainulainen.spring.jooq.common.TestDateUtil;
import net.petrikainulainen.spring.jooq.config.PersistenceContext;
import net.petrikainulainen.spring.jooq.todo.exception.TodoNotFoundException;
import net.petrikainulainen.spring.jooq.todo.model.Todo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static net.petrikainulainen.spring.jooq.todo.model.TodoAssert.assertThatTodo;
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

    private static final String CURRENT_CREATION_TIME = "2012-10-21 11:13:28";
    private static final String CURRENT_DESCRIPTION = "Lorem ipsum";
    private static final String CURRENT_MODIFICATION_TIME = "2012-10-21 11:13:28";
    private static final String CURRENT_TITLE_FIRST_TODO = "Foo";
    private static final String CURRENT_TITLE_SECOND_TODO = "Bar";

    private static final String NEW_CREATION_TIME = TestDateUtil.CURRENT_TIMESTAMP;
    private static final String NEW_DESCRIPTION = "description";
    private static final String NEW_MODIFICATION_TIME = TestDateUtil.CURRENT_TIMESTAMP;
    private static final String NEW_TITLE = "title";

    @Autowired
    private TodoRepository repository;

    @Test
    @DatabaseSetup("empty-todo-data.xml")
    @ExpectedDatabase(value="todo-data-add.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add_ShouldAddNewTodoEntry() {
        Todo newTodoEntry = Todo.getBuilder(NEW_TITLE)
                .description(NEW_DESCRIPTION)
                .build();

        Todo persistedTodoEntry = repository.add(newTodoEntry);

        assertThatTodo(persistedTodoEntry)
                .hasId()
                .hasDescription(NEW_DESCRIPTION)
                .hasTitle(NEW_TITLE)
                .wasCreatedAt(NEW_CREATION_TIME)
                .wasModifiedAt(NEW_MODIFICATION_TIME);
    }

    @Test
    @DatabaseSetup("todo-data.xml")
    @ExpectedDatabase("todo-data-deleted.xml")
    public void delete_TodoFound_ShouldDeleteTodo() {
        Todo deletedTodoEntry = repository.delete(1L);

        assertThatTodo(deletedTodoEntry)
                .hasId(1L)
                .hasDescription(CURRENT_DESCRIPTION)
                .hasTitle(CURRENT_TITLE_FIRST_TODO)
                .wasCreatedAt(CURRENT_CREATION_TIME)
                .wasModifiedAt(CURRENT_MODIFICATION_TIME);
    }

    @Test
    @DatabaseSetup("todo-data.xml")
    @ExpectedDatabase("todo-data.xml")
    public void findAll_TwoTodosFound_ShouldReturnTwoTodoEntries() {
        List<Todo> todoEntries = repository.findAll();

        assertThat(todoEntries).hasSize(2);

        Todo firstTodoEntry = todoEntries.get(0);
        assertThatTodo(firstTodoEntry)
                .hasId(1L)
                .hasDescription(CURRENT_DESCRIPTION)
                .hasTitle(CURRENT_TITLE_FIRST_TODO)
                .wasCreatedAt(CURRENT_CREATION_TIME)
                .wasModifiedAt(CURRENT_MODIFICATION_TIME);

        Todo secondTodoEntry = todoEntries.get(1);
        assertThatTodo(secondTodoEntry)
                .hasId(2L)
                .hasDescription(CURRENT_DESCRIPTION)
                .hasTitle(CURRENT_TITLE_SECOND_TODO)
                .wasCreatedAt(CURRENT_CREATION_TIME)
                .wasModifiedAt(CURRENT_MODIFICATION_TIME);
    }

    @Test
    @DatabaseSetup("todo-data.xml")
    @ExpectedDatabase("todo-data.xml")
    public void findById_TodoFound_ShouldReturnTodo() {
        Todo foundTodoEntry = repository.findById(1L);

        assertThatTodo(foundTodoEntry)
                .hasId(1L)
                .hasDescription(CURRENT_DESCRIPTION)
                .hasTitle(CURRENT_TITLE_FIRST_TODO)
                .wasCreatedAt(CURRENT_CREATION_TIME)
                .wasModifiedAt(CURRENT_MODIFICATION_TIME);
    }

    @Test
    @DatabaseSetup("todo-data.xml")
    @ExpectedDatabase("todo-data.xml")
    public void findById_TodoNotFound_ShouldThrowException() {
        catchException(repository).findById(999L);
        assertThat(caughtException()).isExactlyInstanceOf(TodoNotFoundException.class);
    }

    @Test
    @DatabaseSetup("todo-data.xml")
    @ExpectedDatabase("todo-data.xml")
    public void update_TodoNotFound_ShouldThrowException() {
        Todo updatedTodoEntry = Todo.getBuilder("title")
                .description("description")
                .id(999L)
                .build();

        catchException(repository).update(updatedTodoEntry);
        assertThat(caughtException()).isExactlyInstanceOf(TodoNotFoundException.class);
    }

    @Test
    @DatabaseSetup("todo-data.xml")
    @ExpectedDatabase(value="todo-data-updated.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void update_TodoFound_ShouldUpdateTodo() {
        Todo updatedTodoEntry = Todo.getBuilder(NEW_TITLE)
                .description(NEW_DESCRIPTION)
                .id(2L)
                .build();

        Todo returnedTodoEntry = repository.update(updatedTodoEntry);

        assertThatTodo(returnedTodoEntry)
                .hasId(2L)
                .hasDescription(NEW_DESCRIPTION)
                .hasTitle(NEW_TITLE)
                .wasCreatedAt(CURRENT_CREATION_TIME)
                .wasModifiedAt(NEW_MODIFICATION_TIME);
    }
}
