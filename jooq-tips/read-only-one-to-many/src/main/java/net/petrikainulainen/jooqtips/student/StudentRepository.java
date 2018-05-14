package net.petrikainulainen.jooqtips.student;

import org.jooq.DSLContext;
import org.simpleflatmapper.jdbc.JdbcMapper;
import org.simpleflatmapper.jdbc.JdbcMapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.petrikainulainen.jooqtips.db.Tables.BOOKS;
import static net.petrikainulainen.jooqtips.db.Tables.STUDENTS;

/**
 * Provides finder methods used to query the student information.
 */
@Repository
class StudentRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentRepository.class);

    private final DSLContext jooq;

    @Autowired
    StudentRepository(DSLContext jooq) {
        this.jooq = jooq;
    }

    /**
     * Finds all students.
     * @return  A list that contains all students found from the database.
     *          If no students is found, this method returns an empty list.
     * @throws DataQueryException   when the {@code ResultSet} object cannot be transformed into a list
     *                              because an exception was thrown.
     */
    @Transactional(readOnly = true)
    public List<StudentDTO> findAll() {
        LOGGER.info("Finding all students");

        ResultSet rs = jooq.select(STUDENTS.ID,
                STUDENTS.NAME,
                BOOKS.ID.as("books_id"),
                BOOKS.NAME.as("books_name")
        )
                .from(STUDENTS)
                .leftJoin(BOOKS).on(BOOKS.STUDENT_ID.eq(STUDENTS.ID))
                .orderBy(STUDENTS.ID.asc(), BOOKS.ID.asc())
                .fetchResultSet();

        List<StudentDTO> students = transformQueryResultIntoList(rs);
        LOGGER.info("Found {} students", students.size());

        return students;
    }

    private List<StudentDTO> transformQueryResultIntoList(ResultSet rs) {
        try {
            JdbcMapper<StudentDTO> jdbcMapper = createJdbcMapper();
            return jdbcMapper.stream(rs).collect(Collectors.toList());
        } catch (SQLException ex) {
            LOGGER.error("Cannot transform query result into a list because an error occurred", ex);
            throw new DataQueryException("Cannot transform query result into a list because an error occurred", ex);
        }
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
        LOGGER.info("Finding student by id: {}", id);

        ResultSet rs = jooq.select(STUDENTS.ID,
                STUDENTS.NAME,
                BOOKS.ID.as("books_id"),
                BOOKS.NAME.as("books_name")
        )
                .from(STUDENTS)
                .leftJoin(BOOKS).on(BOOKS.STUDENT_ID.eq(STUDENTS.ID))
                .where(STUDENTS.ID.eq(id))
                .orderBy(BOOKS.ID.asc())
                .fetchResultSet();

        Optional<StudentDTO> student = transformQueryResultIntoObject(rs);
        LOGGER.info("Found student: {}", student);

        return student;
    }

    private Optional<StudentDTO> transformQueryResultIntoObject(ResultSet rs) {
        //JdbcMapper<StudentDTO> jdbcMapper = createJdbcMapper();
        //return jdbcMapper.map(rs);
        List<StudentDTO> students = transformQueryResultIntoList(rs);
        switch (students.size()) {
            case 0:
                return Optional.empty();
            case 1:
                return Optional.of(students.get(0));
            default:
                throw new DataQueryException(
                        "Cannot transform query result into an object because %d rows was found",
                        students.size()
                );
        }
    }

    private JdbcMapper<StudentDTO> createJdbcMapper() {
        return JdbcMapperFactory
                .newInstance()
                .addKeys("id", "books_id")
                .newMapper(StudentDTO.class);
    }
}
