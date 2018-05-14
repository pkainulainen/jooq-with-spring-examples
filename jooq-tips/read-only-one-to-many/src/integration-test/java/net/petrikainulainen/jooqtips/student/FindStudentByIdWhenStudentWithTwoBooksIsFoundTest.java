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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DbUnitJooqTest
@Import(StudentRepository.class)
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@DatabaseSetup({"students-and-books.xml"})
@DisplayName("Find the information of a student when student with two books is found")
class FindStudentByIdWhenStudentWithTwoBooksIsFoundTest {

    @Autowired
    private StudentRepository repository;

    @Test
    @DisplayName("Should return the found student")
    void shouldReturnFoundStudent() {
        Optional<StudentDTO> student = repository.findById(Students.PetriKainulainen.ID);
        assertThat(student).isPresent();
    }

    @Test
    @DisplayName("Should return a student with the correct id")
    void shouldReturnStudentWithCorrectId() {
        StudentDTO student = repository.findById(Students.PetriKainulainen.ID).get();
        assertThat(student.getId()).isEqualByComparingTo(Students.PetriKainulainen.ID);
    }

    @Test
    @DisplayName("Should return a student with the correct name")
    void shouldReturnStudentWithCorrectName() {
        StudentDTO student = repository.findById(Students.PetriKainulainen.ID).get();
        assertThat(student.getName()).isEqualTo(Students.PetriKainulainen.NAME);
    }

    @Test
    @DisplayName("Should return a student that has two books")
    void shouldReturnStudentThatHasTwoBooks() {
        List<BookDTO> books = repository.findById(Students.PetriKainulainen.ID).get()
                .getBooks();
        assertThat(books).hasSize(2);
    }

    @Test
    @DisplayName("Should return the first book with the correct information")
    void shouldReturnFirstBookWithCorrectInformation() {
        BookDTO firstBook = repository.findById(Students.PetriKainulainen.ID).get()
                .getBooks()
                .get(0);

        assertThat(firstBook.getId()).isEqualByComparingTo(Books.LearnJavaIn21Days.ID);
        assertThat(firstBook.getName()).isEqualTo(Books.LearnJavaIn21Days.NAME);
    }

    @Test
    @DisplayName("Should return the second book with the correct information")
    void shouldReturnSecondBookWithCorrectInformation() {
        BookDTO secondBook = repository.findById(Students.PetriKainulainen.ID).get()
                .getBooks()
                .get(1);

        assertThat(secondBook.getId()).isEqualByComparingTo(Books.EffectiveJava.ID);
        assertThat(secondBook.getName()).isEqualTo(Books.EffectiveJava.NAME);
    }
}
