package net.petrikainulainen.spring.jooq.todo.service;

import net.petrikainulainen.spring.jooq.todo.dto.TodoDTO;
import net.petrikainulainen.spring.jooq.todo.model.Todo;
import net.petrikainulainen.spring.jooq.todo.repository.TodoRepository;
import org.jtransfo.JTransfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Petri Kainulainen
 */
@Service
public class RepositoryTodoSearchService implements TodoSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryTodoSearchService.class);

    private TodoRepository repository;

    private JTransfo transformer;

    @Autowired
    public RepositoryTodoSearchService(TodoRepository repository, JTransfo transformer) {
        this.repository = repository;
        this.transformer = transformer;
    }

    @Transactional(readOnly = true)
    @Override
    public List<TodoDTO> findBySearchTerm(String searchTerm) {
        LOGGER.info("Finding todo entry by using search term: {}", searchTerm);

        List<Todo> searchResults = repository.findBySearchTerm(searchTerm);
        LOGGER.info("Found {} todo entries", searchResults.size());

        return transformer.convertList(searchResults, TodoDTO.class);
    }
}
