package net.petrikainulainen.spring.jooq.todo.repository;

import net.petrikainulainen.spring.jooq.todo.exception.NotFoundException;
import net.petrikainulainen.spring.jooq.todo.model.Todo;
import org.jooq.Record;
import org.jooq.impl.DefaultDSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * This was implemented only to ensure that the transaction configuration is working.
 * IT DOESN'T FOLLOW THE BEST PRACTICES OF QUERY GENERATION AND YOU SHOULD NOT USE
 * THIS CODE!!!
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

        Record queryResult = jooq.select()
                .from("todos")
                .where("id=?", id)
                .fetchOne();

        LOGGER.debug("Got result: {}", queryResult);

        if (queryResult == null) {
            throw new NotFoundException("No todo found with id: " + id);
        }

        return Todo.getBuilder(queryResult.getValue("TITLE", String.class))
                .description(queryResult.getValue("DESCRIPTION", String.class))
                .id(queryResult.getValue("ID", Long.class))
                .build();
    }

    @Transactional
    @Override
    public void update(Todo updated) {
        LOGGER.info("Updating todo: {}", updated);

        jooq.execute("update todos set description=?, title=? where id=?",
                updated.getDescription(),
                updated.getTitle(),
                updated.getId()
        );

        LOGGER.info("Todo: {} was updated", updated);
    }

    @Transactional
    @Override
    public void updateAndThrowException(Todo updated) {
        LOGGER.info("Updating todo: {}", updated);

        jooq.execute("update todos set description=?, title=? where id=?",
                updated.getDescription(),
                updated.getTitle(),
                updated.getId()
        );

        LOGGER.info("Todo: {} was updated", updated);

        //This should throw a DataAccessException because the table 'foos' does not exist.
        jooq.select()
                .from("foos")
                .fetch();
    }

}
