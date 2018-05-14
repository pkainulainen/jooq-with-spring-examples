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
@DisplayName("Find the information of all students when one student with two books is found")
class FindAllStudentsWhenOneStudentWithTwoBooksIsFoundTest {

    @Autowired
    private StudentRepository repository;

    @Test
    @DisplayName("Should return one student")
    void shouldReturnOneStudent() {
        List<StudentDTO> students = repository.findAll();
        assertThat(students).hasSize(1);
    }

    @Test
    @DisplayName("Should return a student with the correct id")
    void shouldReturnStudentWithCorrectId() {
        StudentDTO student = repository.findAll().get(0);
        assertThat(student.getId()).isEqualByComparingTo(Students.PetriKainulainen.ID);
    }

    @Test
    @DisplayName("Should return a student with the correct name")
    void shouldReturnStudentWithCorrectName() {
        StudentDTO student = repository.findAll().get(0);
        assertThat(student.getName()).isEqualTo(Students.PetriKainulainen.NAME);
    }

    @Test
    @DisplayName("Should return a student that has two books")
    void shouldReturnStudentThatHasTwoBooks() {
        List<BookDTO> books = repository.findAll()
                .get(0)
                .getBooks();
        assertThat(books).hasSize(2);
    }

    @Test
    @DisplayName("Should return the first book with the correct information")
    void shouldReturnFirstBookWithCorrectInformation() {
        BookDTO firstBook = repository.findAll()
                .get(0)
                .getBooks()
                .get(0);

        assertThat(firstBook.getId()).isEqualByComparingTo(Books.LearnJavaIn21Days.ID);
        assertThat(firstBook.getName()).isEqualTo(Books.LearnJavaIn21Days.NAME);
    }

    @Test
    @DisplayName("Should return the second book with the correct information")
    void shouldReturnSecondBookWithCorrectInformation() {
        BookDTO secondBook = repository.findAll()
                .get(0)
                .getBooks()
                .get(1);

        assertThat(secondBook.getId()).isEqualByComparingTo(Books.EffectiveJava.ID);
        assertThat(secondBook.getName()).isEqualTo(Books.EffectiveJava.NAME);
    }
}
