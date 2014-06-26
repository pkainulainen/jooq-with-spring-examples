package net.petrikainulainen.spring.jooq.todo.service;

import net.petrikainulainen.spring.jooq.todo.dto.TodoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Petri Kainulainen
 */
public interface TodoSearchService {

    public Page<TodoDTO> findBySearchTerm(String searchTerm, Pageable pageable);
}
