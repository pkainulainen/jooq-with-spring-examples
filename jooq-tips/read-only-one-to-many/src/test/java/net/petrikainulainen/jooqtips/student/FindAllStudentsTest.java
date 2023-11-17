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
@DisplayName("Find all students")
class FindAllStudentsTest {

    private final StudentRepository repository;

    @Autowired
    FindAllStudentsTest(DSLContext jooq) {
        this.repository = new StudentRepository(jooq);
    }

    @Nested
    @Sql({
            "/db/clear-database.sql"
    })
    @DisplayName("When no students is found from the database")
    class WhenNoStudentsIsFoundFromDatabase {

        @Test
        @DisplayName("Should return an empty list")
        void shouldReturnEmptyList() {
            var students = repository.findAll();
            assertThat(students).isEmpty();
        }
    }

    @Nested
    @Sql({
            "/db/clear-database.sql",
            "/db/init-students.sql",
            "/db/init-books.sql"
    })
    @DisplayName("When two students is found from the database")
    class WhenTwoStudentsIsFoundFromDatabase {

        @Test
        @DisplayName("Should return two students")
        void shouldReturnTwoStudents() {
            var students = repository.findAll();
            assertThat(students).hasSize(2);
        }

        @Test
        @DisplayName("Should return the information of the first student")
        void shouldReturnInformationOfFirstStudent() {
            var student = repository.findAll().get(0);
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
        @DisplayName("Should return the first student who has two books")
        void shouldReturnFirstStudentWhoHasTwoBooks() {
            var student = repository.findAll().get(0);
            assertThat(student.getBooks()).hasSize(Students.PetriKainulainen.Books.COUNT);
        }

        @Test
        @DisplayName("Should return the information of first student's first book")
        void shouldReturnInformationOfFirstStudentsFirstBook() {
            var book = repository.findAll()
                    .get(0)
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
        @DisplayName("Should return the information of first student's second book")
        void shouldReturnInformationOfFirstStudentsSecondBook() {
            var book = repository.findAll()
                    .get(0)
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

        @Test
        @DisplayName("Should return the information of the second student")
        void shouldReturnInformationOfSecondStudent() {
            var student = repository.findAll().get(1);
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(student.getId())
                        .as("id")
                        .isEqualTo(Students.Lukas.ID);
                softAssertions.assertThat(student.getName())
                        .as("name")
                        .isEqualTo(Students.Lukas.NAME);
            });
        }

        @Test
        @DisplayName("Should return the second student who has one book")
        void shouldReturnSecondStudentWhoHasOneBook() {
            var student = repository.findAll().get(1);
            assertThat(student.getBooks()).hasSize(Students.Lukas.Books.COUNT);
        }

        @Test
        @DisplayName("Should return the information of second student's only book")
        void shouldReturnInformationOfSecondStudentsOnlyBook() {
            var book = repository.findAll()
                    .get(1)
                    .getBooks()
                    .get(0);
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(book.getId())
                        .as("id")
                        .isEqualTo(Students.Lukas.Books.JOOQInAction.ID);
                softAssertions.assertThat(book.getName())
                        .as("name")
                        .isEqualTo(Students.Lukas.Books.JOOQInAction.NAME);
            });
        }
    }
}
