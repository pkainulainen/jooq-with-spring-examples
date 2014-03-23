package net.petrikainulainen.spring.jooq.todo.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.petrikainulainen.spring.jooq.common.json.CustomLocalDateTimeDeserializer;
import net.petrikainulainen.spring.jooq.common.json.CustomLocalDateTimeSerializer;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;
import org.jtransfo.DomainClass;

/**
 * @author Petri Kainulainen
 */
@DomainClass("net.petrikainulainen.spring.jooq.todo.model.Todo")
public class TodoDTO {

    private Long id;

    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime creationTime;

    @Length(max = 500)
    private String description;

    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime modificationTime;

    @NotEmpty
    @Length(max = 100)
    private String title;

    public TodoDTO() {

    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getModificationTime() {
        return modificationTime;
    }

    public String getTitle() {
        return title;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setModificationTime(LocalDateTime modificationTime) {
        this.modificationTime = modificationTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("creationTime", creationTime)
                .append("description", description)
                .append("modificationTime", modificationTime)
                .append("title", title)
                .build();

    }
}
