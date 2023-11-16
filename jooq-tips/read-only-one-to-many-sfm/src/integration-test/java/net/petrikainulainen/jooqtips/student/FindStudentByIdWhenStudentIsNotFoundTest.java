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
@DatabaseSetup({"no-students-and-books.xml"})
@DisplayName("Find the information of a student when the student is not found")
class FindStudentByIdWhenStudentIsNotFoundTest {

    @Autowired
    private StudentRepository repository;

    @Test
    @DisplayName("Should return empty optional")
    void shouldReturnEmptyOptional() {
        Optional<StudentDTO> student = repository.findById(Students.PetriKainulainen.ID);
        assertThat(student).isNotPresent();
    }
}
