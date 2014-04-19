package net.petrikainulainen.spring.jooq.todo.service;

import net.petrikainulainen.spring.jooq.todo.dto.TodoDTO;

import java.util.List;

/**
 * @author Petri Kainulainen
 */
public interface TodoSearchService {

    public List<TodoDTO> findBySearchTerm(String searchTerm);
}
