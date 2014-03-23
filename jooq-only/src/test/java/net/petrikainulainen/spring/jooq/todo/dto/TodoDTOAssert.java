package net.petrikainulainen.spring.jooq.todo.dto;

import net.petrikainulainen.spring.jooq.common.TestDateUtil;
import org.assertj.core.api.AbstractAssert;
import org.joda.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petri Kainulainen
 */
public class TodoDTOAssert extends AbstractAssert<TodoDTOAssert, TodoDTO> {

    private TodoDTOAssert(TodoDTO actual) {
        super(actual, TodoDTOAssert.class);
    }

    public static TodoDTOAssert assertThatTodoDTO(TodoDTO actual) {
        return new TodoDTOAssert(actual);
    }

    public TodoDTOAssert hasDescription(String description) {
        isNotNull();

        assertThat(actual.getDescription())
                .overridingErrorMessage("Expected description to be <%s> but was <%s>", description, actual.getDescription())
                .isEqualTo(description);

        return this;
    }

    public TodoDTOAssert hasId(Long id) {
        isNotNull();

        assertThat(actual.getId())
                .overridingErrorMessage("Expected id to be <%d> but was <%d>", id, actual.getId())
                .isEqualTo(id);

        return this;
    }

    public TodoDTOAssert hasNoCreationTime() {
        isNotNull();

        assertThat(actual.getCreationTime())
                .overridingErrorMessage("Expected creationTime to be <null> but was <%s>", actual.getCreationTime())
                .isNull();

        return this;
    }

    public TodoDTOAssert hasNoId() {
        isNotNull();

        assertThat(actual.getId())
                .overridingErrorMessage("Expected id to be <null> but was <%d>", actual.getId())
                .isNull();

        return this;
    }

    public TodoDTOAssert hasNoModificationTime() {
        isNotNull();

        assertThat(actual.getModificationTime())
                .overridingErrorMessage("Expected modificationTime to be <null> but was <%s>", actual.getModificationTime())
                .isNull();

        return this;
    }

    public TodoDTOAssert hasTitle(String title) {
        isNotNull();

        assertThat(actual.getTitle())
                .overridingErrorMessage("Expected title to be <%s> but was <%s>", title, actual.getTitle())
                .isEqualTo(title);

        return this;
    }

    public TodoDTOAssert wasCreatedAt(String creationTime) {
        isNotNull();

        LocalDateTime createdAt = TestDateUtil.parseLocalDateTime(creationTime);

        assertThat(actual.getCreationTime())
                .overridingErrorMessage("Expected creationTime to be <%s> but was <%s>.", createdAt, actual.getCreationTime())
                .isEqualTo(createdAt);

        return this;
    }

    public TodoDTOAssert wasModifiedAt(String modificationTime) {
        isNotNull();

        LocalDateTime modifiedAt = TestDateUtil.parseLocalDateTime(modificationTime);

        assertThat(actual.getModificationTime())
                .overridingErrorMessage("Expected moficationTime to be <%s> but was <%s>", modifiedAt, actual.getModificationTime())
                .isEqualTo(modifiedAt);

        return this;
    }
}
