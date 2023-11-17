package net.petrikainulainen.jooqtips.student;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Contains the information of a single book.
 */
public class BookDTO {

    private Long id;
    private String name;

    public BookDTO() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.id)
                .append("name", this.name)
                .build();
    }
}
