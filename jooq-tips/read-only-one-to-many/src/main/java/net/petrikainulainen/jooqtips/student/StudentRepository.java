package net.petrikainulainen.jooqtips.student;

import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.SelectJoinStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static net.petrikainulainen.jooqtips.Tables.BOOKS;
import static net.petrikainulainen.jooqtips.Tables.STUDENTS;
import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.selectDistinct;

/**
 * Provides finder methods used to query the student information.
 */
@Repository
public class StudentRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentRepository.class);

    private final DSLContext jooq;

    @Autowired
    StudentRepository(DSLContext jooq) {
        this.jooq = jooq;
    }

    /**
     * Returns a list that contains all students found from the database.
     * If no students is found, this method returns an empty list.
     */
    @Transactional(readOnly = true)
    public List<StudentDTO> findAll() {
        LOGGER.debug("Finding all students");
        var students = buildSelectStep()
                .orderBy(STUDENTS.ID.asc())
                .fetchInto(StudentDTO.class);

        LOGGER.debug("Found {} students", students.size());
        return students;
    }

    /**
     * Finds the information of the requested student.
     * @param id    The id of the requested student.
     * @return      An {@code Optional} that contains the found student.
     *              If no student is found, this method returns an empty
     *              {@code Optional} object.
     */
    @Transactional(readOnly = true)
    public Optional<StudentDTO> findById(Long id) {
        LOGGER.debug("Finding student by id: {}", id);
        var queryResult = buildSelectStep()
                .where(STUDENTS.ID.eq(id))
                .fetchOptional();
        var student = queryResult.map(r -> r.into(StudentDTO.class));
        LOGGER.debug("Found student: {} by id: {}", student, id);

        return student;
    }

    private SelectJoinStep<Record3<Long, String, List<BookDTO>>> buildSelectStep() {
        return jooq.select(
                        STUDENTS.ID,
                        STUDENTS.NAME,
                        multiset(
                                selectDistinct(
                                        BOOKS.ID,
                                        BOOKS.NAME
                                )
                                        .from(BOOKS)
                                        .where(BOOKS.STUDENT_ID.eq(STUDENTS.ID))
                        ).as("books").convertFrom(r -> r.into(BookDTO.class))
                )
                .from(STUDENTS);
    }
}
