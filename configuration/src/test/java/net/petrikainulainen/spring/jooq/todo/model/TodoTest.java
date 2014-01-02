package net.petrikainulainen.spring.jooq.todo.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petri Kainulainen
 */
public class TodoTest {

    private static final Long ID = 1L;
    private static final String DESCRIPTION = "description";
    private static final String TITLE = "title";

    @Test
    public void build_titleIsSet_shouldCreateNewObjectAndSetTitle() {
        Todo created = Todo.getBuilder(TITLE).build();

        assertThat(created.getId()).isNull();
        assertThat(created.getDescription()).isNull();
        assertThat(created.getTitle()).isEqualTo(TITLE);
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
                .description(DESCRIPTION)
                .id(ID)
                .build();

        assertThat(created.getId()).isEqualTo(ID);
        assertThat(created.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(created.getTitle()).isEqualTo(TITLE);
    }
}
