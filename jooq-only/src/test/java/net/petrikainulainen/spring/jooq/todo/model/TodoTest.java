package net.petrikainulainen.spring.jooq.todo.model;

import net.petrikainulainen.spring.jooq.common.TestDateUtil;
import org.junit.Test;

import java.sql.Timestamp;

import static net.petrikainulainen.spring.jooq.todo.model.TodoAssert.assertThatTodo;

/**
 * @author Petri Kainulainen
 */
public class TodoTest {

    private static final Long ID = 1L;
    private static final String DESCRIPTION = "description";
    private static final String TIMESTAMP_STRING = "2014-02-18 11:13:28";
    private static final Timestamp TIMESTAMP = TestDateUtil.parseTimestamp(TIMESTAMP_STRING);
    private static final String TITLE = "title";

    @Test
    public void build_titleIsSet_shouldCreateNewObjectAndSetTitle() {
        Todo created = Todo.getBuilder(TITLE).build();

        assertThatTodo(created)
                .hasNoDescription()
                .hasNoId()
                .hasTitle(TITLE)
                .creationTimeIsNotSet()
                .modificationTimeIsNotSet();
    }

    @Test(expected = IllegalStateException.class)
    public void build_titleIsNull_ShouldThrowException() {
        Todo.getBuilder(null).build();
    }

    @Test(expected = IllegalStateException.class)
    public void build_titleIsEmpty_ShouldThrowException() {
        Todo.getBuilder("").build();
    }

    @Test
    public void build_setAllValues_shouldCreateNewObjectAndSetAllFields() {
        Todo created = Todo.getBuilder(TITLE)
                .creationTime(TIMESTAMP)
                .description(DESCRIPTION)
                .id(ID)
                .modificationTime(TIMESTAMP)
                .build();

        assertThatTodo(created)
                .hasDescription(DESCRIPTION)
                .hasId(ID)
                .hasTitle(TITLE)
                .wasCreatedAt(TIMESTAMP_STRING)
                .wasModifiedAt(TIMESTAMP_STRING);
    }
}
