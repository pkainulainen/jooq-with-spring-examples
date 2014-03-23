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
public class RepositoryTodoCrudService implements TodoCrudService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryTodoCrudService.class);

    private final TodoRepository repository;

    private final JTransfo transformer;

    @Autowired
    public RepositoryTodoCrudService(TodoRepository repository, JTransfo transformer) {
        this.repository = repository;
        this.transformer = transformer;
    }

    @Transactional
    @Override
    public TodoDTO add(TodoDTO dto) {
        LOGGER.info("Adding todo entry with information: {}", dto);

        Todo added = createModel(dto);
        Todo persisted = repository.add(added);

        LOGGER.info("Added todo entry with information: {}", persisted);

        return transformer.convert(persisted, new TodoDTO());
    }

    @Transactional
    @Override
    public TodoDTO delete(Long id) {
        LOGGER.info("Deleting todo entry with id: {}", id);

        Todo deleted = repository.delete(id);

        LOGGER.info("Deleted to entry with id: {}", id);

        return transformer.convert(deleted, new TodoDTO());
    }

    @Transactional(readOnly = true)
    @Override
    public List<TodoDTO> findAll() {
        LOGGER.info("Finding all todo entries.");

        List<Todo> todoEntries = repository.findAll();

        LOGGER.debug("Found {} todo entries.", todoEntries.size());

        return transformer.convertList(todoEntries, TodoDTO.class);
    }

    @Transactional(readOnly = true)
    @Override
    public TodoDTO findById(Long id) {
        LOGGER.info("Finding to entry with id: {}", id);

        Todo found = repository.findById(id);

        LOGGER.info("Found todo entry: {}", found);

        return transformer.convert(found, new TodoDTO());
    }

    @Transactional
    @Override
    public TodoDTO update(TodoDTO dto) {
        LOGGER.info("Updating the information of a todo entry: {}", dto);

        Todo newInformation = createModel(dto);
        Todo updated = repository.update(newInformation);

        LOGGER.debug("Updated the information of a todo entry: {}", updated);

        return transformer.convert(updated, new TodoDTO());
    }

    private Todo createModel(TodoDTO dto) {
        return Todo.getBuilder(dto.getTitle())
                .description(dto.getDescription())
                .id(dto.getId())
                .build();
    }
}
