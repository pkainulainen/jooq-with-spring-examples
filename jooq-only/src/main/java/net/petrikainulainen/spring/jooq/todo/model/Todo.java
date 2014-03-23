package net.petrikainulainen.spring.jooq.todo.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.LocalDateTime;

import java.sql.Timestamp;

/**
 * @author Petri Kainulainen
 */
public class Todo {

    private final Long id;

    private final LocalDateTime creationTime;

    private final String description;

    private final LocalDateTime modificationTime;

    private final String title;

    private Todo(Builder builder) {
        this.id = builder.id;

        LocalDateTime creationTime = null;
        if (builder.creationTime != null) {
            creationTime = new LocalDateTime(builder.creationTime);
        }
        this.creationTime = creationTime;

        this.description = builder.description;

        LocalDateTime modificationTime = null;
        if (builder.modificationTime != null) {
            modificationTime = new LocalDateTime(builder.modificationTime);
        }
        this.modificationTime = modificationTime;

        this.title = builder.title;
    }

    public static Builder getBuilder(String title) {
        return new Builder(title);
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

    public static class Builder {

        private Long id;

        private Timestamp creationTime;

        private String description;

        private Timestamp modificationTime;

        private String title;

        public Builder(String title) {
            this.title = title;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder creationTime(Timestamp creationTime) {
            this.creationTime = creationTime;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder modificationTime(Timestamp modificationTime) {
            this.modificationTime = modificationTime;
            return this;
        }

        public Todo build() {
            Todo created = new Todo(this);

            String title = created.getTitle();

            if (title == null || title.length() == 0) {
                throw new IllegalStateException("title cannot be null or empty");
            }

            return created;
        }
    }
}
