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
@DatabaseSetup({"students-with-no-books.xml"})
@DisplayName("Find the information of a student when student with zero books is found")
class FindStudentByIdWhenStudentWithZeroBooksIsFoundTest {

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
    @DisplayName("Should return a student that has no books")
    void shouldReturnStudentThatHasNoBooks() {
        List<BookDTO> books = repository.findById(Students.PetriKainulainen.ID).get()
                .getBooks();
        assertThat(books).isEmpty();
    }
}
