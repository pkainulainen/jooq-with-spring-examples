package net.petrikainulainen.jooqtips.student;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Contains the information of a single student.
 */
public class StudentDTO {

    private Long id;
    private String name;
    private List<BookDTO> books;

    public StudentDTO() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<BookDTO> getBooks() {
        return books;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBooks(List<BookDTO> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.id)
                .append("name", this.name)
                .build();
    }
}
