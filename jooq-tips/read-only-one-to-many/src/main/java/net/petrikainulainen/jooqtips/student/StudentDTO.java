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
                //I include the book list in the output because I know
                //that it contains only a few books (because this is an
                //example). It might not be wise to use the same approach
                //in real life applications because you don't necessarily
                //know how many items a list can have (or a collection).
                .append("books", this.books)
                .build();
    }
}
