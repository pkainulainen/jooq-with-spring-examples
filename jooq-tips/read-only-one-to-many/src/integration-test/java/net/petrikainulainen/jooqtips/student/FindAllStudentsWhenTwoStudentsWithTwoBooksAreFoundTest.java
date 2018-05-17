package net.petrikainulainen.jooqtips.student;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;
import net.petrikainulainen.jooqtips.DbUnitJooqTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DbUnitJooqTest
@Import(StudentRepository.class)
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@DatabaseSetup({"students-and-books.xml"})
@DisplayName("Find the information of all students when two students with two books is found")
class FindAllStudentsWhenTwoStudentsWithTwoBooksAreFoundTest {

    @Autowired
    private StudentRepository repository;

    @Test
    @DisplayName("Should return two students")
    void shouldReturnTwoStudents() {
        List<StudentDTO> students = repository.findAll();
        assertThat(students).hasSize(2);
    }

    @Test
    @DisplayName("Should return the first student with the correct id")
    void shouldReturnFirstStudentWithCorrectId() {
        StudentDTO student = repository.findAll().get(0);
        assertThat(student.getId()).isEqualByComparingTo(Students.PetriKainulainen.ID);
    }

    @Test
    @DisplayName("Should return the first student with the correct name")
    void shouldReturnFirstStudentWithCorrectName() {
        StudentDTO student = repository.findAll().get(0);
        assertThat(student.getName()).isEqualTo(Students.PetriKainulainen.NAME);
    }

    @Test
    @DisplayName("Should return the first student that has two books")
    void shouldReturnFirstStudentThatHasTwoBooks() {
        List<BookDTO> books = repository.findAll()
                .get(0)
                .getBooks();
        assertThat(books).hasSize(2);
    }

    @Test
    @DisplayName("Should return the first book of the first student with the correct information")
    void shouldReturnFirstBookOfFirstStudentWithCorrectInformation() {
        BookDTO firstBook = repository.findAll()
                .get(0)
                .getBooks()
                .get(0);

        assertThat(firstBook.getId()).isEqualByComparingTo(Books.LearnJavaIn21Days.ID);
        assertThat(firstBook.getName()).isEqualTo(Books.LearnJavaIn21Days.NAME);
    }

    @Test
    @DisplayName("Should return the second book of the first student with the correct information")
    void shouldReturnSecondBookOfFirstStudentWithCorrectInformation() {
        BookDTO secondBook = repository.findAll()
                .get(0)
                .getBooks()
                .get(1);

        assertThat(secondBook.getId()).isEqualByComparingTo(Books.EffectiveJava.ID);
        assertThat(secondBook.getName()).isEqualTo(Books.EffectiveJava.NAME);
    }

    @Test
    @DisplayName("Should return the second student with the correct id")
    void shouldReturnSecondStudentWithCorrectId() {
        StudentDTO student = repository.findAll().get(1);
        assertThat(student.getId()).isEqualByComparingTo(Students.JaneDoe.ID);
    }

    @Test
    @DisplayName("Should return the second student with the correct name")
    void shouldReturnSecondStudentWithCorrectName() {
        StudentDTO student = repository.findAll().get(1);
        assertThat(student.getName()).isEqualTo(Students.JaneDoe.NAME);
    }

    @Test
    @DisplayName("Should return the second student that has two books")
    void shouldReturnSecondStudentThatHasTwoBooks() {
        List<BookDTO> books = repository.findAll()
                .get(1)
                .getBooks();
        assertThat(books).hasSize(2);
    }

    @Test
    @DisplayName("Should return the first book of the second student with the correct information")
    void shouldReturnFirstBookOfSecondStudentWithCorrectInformation() {
        BookDTO firstBook = repository.findAll()
                .get(1)
                .getBooks()
                .get(0);

        assertThat(firstBook.getId()).isEqualByComparingTo(Books.LearnCPlusPlusIn21Days.ID);
        assertThat(firstBook.getName()).isEqualTo(Books.LearnCPlusPlusIn21Days.NAME);
    }

    @Test
    @DisplayName("Should return the second book of the second student with the correct information")
    void shouldReturnSecondBookOfSecondStudentWithCorrectInformation() {
        BookDTO secondBook = repository.findAll()
                .get(1)
                .getBooks()
                .get(1);

        assertThat(secondBook.getId()).isEqualByComparingTo(Books.TheCPlusPlusProgrammingLanguage.ID);
        assertThat(secondBook.getName()).isEqualTo(Books.TheCPlusPlusProgrammingLanguage.NAME);
    }
}
