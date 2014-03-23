package net.petrikainulainen.spring.jooq.todo.service;

import net.petrikainulainen.spring.jooq.todo.dto.TodoDTO;

import java.util.List;

/**
 * @author Petri Kainulainen
 */
public interface TodoCrudService {

    public TodoDTO add(TodoDTO todo);

    public TodoDTO delete(Long id);

    public List<TodoDTO> findAll();

    public TodoDTO findById(Long id);

    public TodoDTO update(TodoDTO updated);
}
