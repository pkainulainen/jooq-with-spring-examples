package net.petrikainulainen.spring.jooq.todo.repository;

import net.petrikainulainen.spring.jooq.todo.model.Todo;

/**
 * @author Petri Kainulainen
 */
public interface TodoRepository {

    public Todo findById(Long id);

    public void update(Todo updated);

    public void updateAndThrowException(Todo updated);
}
