package net.petrikainulainen.spring.jooq.todo.repository;

import net.petrikainulainen.spring.jooq.todo.model.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Petri Kainulainen
 */
public interface TodoRepository {

    /**
     * Adds a new todo entry.
     * @param todoEntry  The information of the added todo entry.
     * @return  The added todo entry.
     */
    public Todo add(Todo todoEntry);

    /**
     * Deletes a todo entry.
     * @param id    The id of the deleted todo entry.
     * @return  The deleted todo entry.
     * @throws net.petrikainulainen.spring.jooq.todo.exception.TodoNotFoundException If the deleted todo entry is not found.
     */
    public Todo delete(Long id);

    /**
     * Finds all todo entries.
     * @return  Found todo entries.
     */
    public List<Todo> findAll();

    /**
     * Finds a todo entry.
     * @param id    The id of the requested todo entry.
     * @return  The found todo entry.
     * @throws net.petrikainulainen.spring.jooq.todo.exception.TodoNotFoundException If todo entry is not found.
     */
    public Todo findById(Long id);

    public Page<Todo> findBySearchTerm(String searchTerm, Pageable pageable);

    /**
     * Updates the information of a todo entry.
     * @param todoEntry   The new information of a todo entry.
     * @return  The updated todo entry.
     * @throws net.petrikainulainen.spring.jooq.todo.exception.TodoNotFoundException If the updated todo entry is not found.
     */
    public Todo update(Todo todoEntry);
}
