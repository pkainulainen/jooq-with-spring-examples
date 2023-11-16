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
@DatabaseSetup({"students-with-no-books.xml"})
@DisplayName("Find the information of all students when one student with no books is found")
class FindAllStudentsWhenOneStudentWithZeroBooksIsFoundTest {

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
    @DisplayName("Should return a student that has zero books")
    void shouldReturnStudentThatHasTwoBooks() {
        List<BookDTO> books = repository.findAll()
                .get(0)
                .getBooks();
        assertThat(books).isEmpty();
    }
}
