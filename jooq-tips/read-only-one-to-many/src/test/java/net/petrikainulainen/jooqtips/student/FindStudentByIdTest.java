package net.petrikainulainen.jooqtips.student;

import org.jooq.DSLContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@JOOQRepositoryTest
@Sql({
        "/db/clear-database.sql",
        "/db/init-students.sql",
        "/db/init-books.sql"
})
@DisplayName("Find students by using id as search criteria")
class FindStudentByIdTest {

    private final StudentRepository repository;

    @Autowired
    FindStudentByIdTest(DSLContext jooq) {
        this.repository = new StudentRepository(jooq);
    }

    @Nested
    @DisplayName("When the requested student isn't found from the database")
    class WhenStudentIsNotFound {

        @Test
        @DisplayName("Should return an empty optional")
        void shouldReturnEmptyOptional() {
            var student = repository.findById(Students.UNKNOWN_ID);
            assertThat(student).isEmpty();
        }
    }

    @Nested
    @DisplayName("When the requested student is found from the database")
    class WhenStudentIsFound {

        @Test
        @DisplayName("Should return an optional that contains the found student")
        void shouldReturnOptionalThatContainsFoundStudent() {
            var student = repository.findById(Students.PetriKainulainen.ID);
            assertThat(student).isNotEmpty();
        }

        @Test
        @DisplayName("Should return a student with the correct information")
        void shouldReturnStudentWithCorrectInformation() {
            var student = repository.findById(Students.PetriKainulainen.ID).get();
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(student.getId())
                        .as("id")
                        .isEqualTo(Students.PetriKainulainen.ID);
                softAssertions.assertThat(student.getName())
                        .as("name")
                        .isEqualTo(Students.PetriKainulainen.NAME);
            });
        }

        @Test
        @DisplayName("Should return a student who has two books")
        void shouldReturnStudentWhoHasTwoBooks() {
            var student = repository.findById(Students.PetriKainulainen.ID).get();
            assertThat(student.getBooks()).hasSize(Students.PetriKainulainen.Books.COUNT);
        }

        @Test
        @DisplayName("Should return the information of the first book")
        void shouldReturnInformationOfFirstBook() {
            var book = repository.findById(Students.PetriKainulainen.ID)
                    .get()
                    .getBooks()
                    .get(0);
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(book.getId())
                        .as("id")
                        .isEqualTo(Students.PetriKainulainen.Books.LearnJavaIn21Days.ID);
                softAssertions.assertThat(book.getName())
                        .as("name")
                        .isEqualTo(Students.PetriKainulainen.Books.LearnJavaIn21Days.NAME);
            });
        }

        @Test
        @DisplayName("Should return the information of the second book")
        void shouldReturnInformationOfSecondBook() {
            var book = repository.findById(Students.PetriKainulainen.ID)
                    .get()
                    .getBooks()
                    .get(1);
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(book.getId())
                        .as("id")
                        .isEqualTo(Students.PetriKainulainen.Books.EffectiveJava.ID);
                softAssertions.assertThat(book.getName())
                        .as("name")
                        .isEqualTo(Students.PetriKainulainen.Books.EffectiveJava.NAME);
            });
        }
    }
}
