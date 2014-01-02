package net.petrikainulainen.spring.jooq.todo.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Petri Kainulainen
 */
public class Todo {

    private Long id;

    private String description;

    private String title;

    private Todo() {

    }

    public static Builder getBuilder(String title) {
        return new Builder(title);
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("description", description)
                .append("title", title)
                .build();
    }

    public static class Builder {

        private Long id;

        private String description;

        private String title;

        public Builder(String title) {
            this.title = title;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Todo build() {
            if (title == null || title.length() == 0) {
                throw new IllegalStateException("title cannot be null or empty");
            }

            Todo created = new Todo();
            created.id = id;
            created.description = description;
            created.title = title;

            return created;
        }
    }
}
