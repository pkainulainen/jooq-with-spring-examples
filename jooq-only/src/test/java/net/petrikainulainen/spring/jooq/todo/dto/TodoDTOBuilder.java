package net.petrikainulainen.spring.jooq.todo.dto;

import net.petrikainulainen.spring.jooq.common.TestDateUtil;
import org.joda.time.LocalDateTime;

/**
 * @author Petri Kainulainen
 */
public class TodoDTOBuilder {

    private Long id;

    private LocalDateTime creationTime;

    private String description;

    private LocalDateTime modificationTime;

    private String title;

    public TodoDTOBuilder() {

    }

    public TodoDTOBuilder creationTime(String creationTime) {
        this.creationTime = TestDateUtil.parseLocalDateTime(creationTime);
        return this;
    }

    public TodoDTOBuilder description(String description) {
        this.description = description;
        return this;
    }

    public TodoDTOBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public TodoDTOBuilder modificationTime(String modificationTime) {
        this.modificationTime = TestDateUtil.parseLocalDateTime(modificationTime);
        return this;
    }

    public TodoDTOBuilder title(String title) {
        this.title = title;
        return this;
    }

    public TodoDTO build() {
        TodoDTO dto = new TodoDTO();

        dto.setId(id);
        dto.setCreationTime(creationTime);
        dto.setDescription(description);
        dto.setModificationTime(modificationTime);
        dto.setTitle(title);

        return dto;
    }
}
