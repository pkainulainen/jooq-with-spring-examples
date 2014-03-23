package net.petrikainulainen.spring.jooq.todo.model;

import net.petrikainulainen.spring.jooq.common.TestDateUtil;
import org.assertj.core.api.AbstractAssert;
import org.joda.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petri Kainulainen
 */
public class TodoAssert extends AbstractAssert<TodoAssert, Todo> {

    private TodoAssert(Todo actual) {
        super(actual, TodoAssert.class);
    }

    public static TodoAssert assertThatTodo(Todo actual) {
        return new TodoAssert(actual);
    }

    public TodoAssert creationTimeIsNotSet() {
        isNotNull();

        assertThat(actual.getCreationTime())
                .overridingErrorMessage("Expected creationTime to be <null> but was <%s>", actual.getCreationTime())
                .isNull();

        return this;
    }

    public TodoAssert hasDescription(String description) {
        isNotNull();

        assertThat(actual.getDescription())
                .overridingErrorMessage("Expected description to be <%s> but was <%s>", description, actual.getDescription())
                .isEqualTo(description);

        return this;
    }

    public TodoAssert hasId() {
        isNotNull();

        assertThat(actual.getId())
                .overridingErrorMessage("Expected id to be not null but was <null>")
                .isNotNull();

        return this;
    }

    public TodoAssert hasId(Long id) {
        isNotNull();

        assertThat(actual.getId())
                .overridingErrorMessage("Expected id to be <%d> but was <%d>", id, actual.getId())
                .isEqualTo(id);

        return this;
    }

    public TodoAssert hasNoDescription() {
        isNotNull();

        assertThat(actual.getDescription())
                .overridingErrorMessage("Expected description to be <null> but was <%s>", actual.getDescription());

        return this;
    }

    public TodoAssert hasNoId() {
        isNotNull();

        assertThat(actual.getId())
                .overridingErrorMessage("Expected id to be <null> but was <%d>", actual.getId())
                .isNull();

        return this;
    }

    public TodoAssert hasTitle(String title) {
        isNotNull();

        assertThat(actual.getTitle())
                .overridingErrorMessage("Expected title to be <%s> but was <%s>", title, actual.getTitle())
                .isEqualTo(title);

        return this;
    }

    public TodoAssert modificationTimeIsNotSet() {
        isNotNull();

        assertThat(actual.getModificationTime())
                .overridingErrorMessage("Expected modificationTime to be <null> but was <%s>", actual.getModificationTime())
                .isNull();

        return this;
    }

    public TodoAssert wasCreatedAt(String creationTime) {
        isNotNull();

        LocalDateTime createdAt = TestDateUtil.parseLocalDateTime(creationTime);

        assertThat(actual.getCreationTime())
                .overridingErrorMessage("Expected creationTime to be <%s> but was <%s>", createdAt, actual.getCreationTime())
                .isEqualTo(createdAt);

        return this;
    }

    public TodoAssert wasModifiedAt(String modificationTime) {
        isNotNull();

        LocalDateTime modifiedAt = TestDateUtil.parseLocalDateTime(modificationTime);

        assertThat(actual.getModificationTime())
                .overridingErrorMessage("Expected modificationTime to be <%s> but was <%s>", modifiedAt, actual.getModificationTime())
                .isEqualTo(modifiedAt);

        return this;
    }
}
