package net.petrikainulainen.spring.jooq.todo.repository;

import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.impl.DefaultDSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
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

    public Integer findOne() {
        LOGGER.info("findOne() method was invoked");

        Result<Record1<Integer>> result = jooq.selectOne().fetch();
        LOGGER.debug("Received result: {}", result);

        return (Integer) result.getValue(0, "1");
    }
}
