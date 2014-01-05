package net.petrikainulainen.spring.jooq.todo.repository;

import net.petrikainulainen.spring.jooq.todo.db.tables.records.TodosRecord;
import net.petrikainulainen.spring.jooq.todo.exception.NotFoundException;
import net.petrikainulainen.spring.jooq.todo.model.Todo;
import org.jooq.impl.DefaultDSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static net.petrikainulainen.spring.jooq.todo.db.tables.Todos.TODOS;

/**
 * This class was implemented only to ensure that the transaction configuration is working.
 * @author Petri Kainulainen
 */
@Repository
public class JOOQTodoRepository implements TodoRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(JOOQTodoRepository.class);

    private DefaultDSLContext jooq;

    @Autowired
    public JOOQTodoRepository(DefaultDSLContext jooq) {
        this.jooq = jooq;
    }

    @Transactional(readOnly = true)
    @Override
    public Todo findById(Long id) {
        LOGGER.info("Finding todo by id: {}", id);

        TodosRecord queryResult = jooq.selectFrom(TODOS)
                .where(TODOS.ID.equal(id))
                .fetchOne();

        LOGGER.debug("Got result: {}", queryResult);

        if (queryResult == null) {
            throw new NotFoundException("No todo found with id: " + id);
        }

        return Todo.getBuilder(queryResult.getTitle())
                .description(queryResult.getDescription())
                .id(queryResult.getId())
                .build();
    }

    @Transactional
    @Override
    public void update(Todo updated) {
        LOGGER.info("Updating todo: {}", updated);

        int updatedRowCount = jooq.update(TODOS)
                .set(TODOS.DESCRIPTION, updated.getDescription())
                .set(TODOS.TITLE, updated.getTitle())
                .where(TODOS.ID.equal(updated.getId()))
                .execute();

        LOGGER.debug("Updated {} rows.", updatedRowCount);

        LOGGER.info("Todo: {} was updated", updated);
    }

    @Transactional
    @Override
    public void updateAndThrowException(Todo updated) {
        LOGGER.info("Updating todo: {}", updated);

        int updatedRowCount = jooq.update(TODOS)
                .set(TODOS.DESCRIPTION, updated.getDescription())
                .set(TODOS.TITLE, updated.getTitle())
                .where(TODOS.ID.equal(updated.getId()))
                .execute();

        LOGGER.debug("Updated {} rows.", updatedRowCount);

        LOGGER.info("Todo: {} was updated", updated);

        //This should throw a DataAccessException because the table 'foos' does not exist.
        jooq.select()
                .from("foos")
                .fetch();
    }

}
