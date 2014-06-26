package net.petrikainulainen.spring.jooq.todo.service;

import net.petrikainulainen.spring.jooq.todo.dto.TodoDTO;
import net.petrikainulainen.spring.jooq.todo.model.Todo;
import net.petrikainulainen.spring.jooq.todo.repository.TodoRepository;
import org.jtransfo.JTransfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Page<TodoDTO> findBySearchTerm(String searchTerm, Pageable pageable) {
        LOGGER.info("Finding {} todo entries for page {} by using search term: {}",
                pageable.getPageSize(),
                pageable.getPageNumber(),
                searchTerm
        );

        Page<Todo> searchResults = repository.findBySearchTerm(searchTerm, pageable);
        LOGGER.info("Found {} todo entries for page: {}",
                searchResults.getNumberOfElements(),
                searchResults.getNumber()
        );

        List<TodoDTO> dtos = transformer.convertList(searchResults.getContent(), TodoDTO.class);

        return new PageImpl<>(dtos,
                new PageRequest(searchResults.getNumber(), searchResults.getSize(), searchResults.getSort()),
                searchResults.getTotalElements()
        );
    }
}
