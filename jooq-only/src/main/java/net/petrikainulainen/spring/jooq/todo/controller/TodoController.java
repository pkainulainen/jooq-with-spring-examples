package net.petrikainulainen.spring.jooq.todo.controller;

import net.petrikainulainen.spring.jooq.todo.dto.TodoDTO;
import net.petrikainulainen.spring.jooq.todo.service.TodoCrudService;
import net.petrikainulainen.spring.jooq.todo.service.TodoSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Petri Kainulainen
 */
@RestController
@RequestMapping("/api/todo")
public class TodoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoController.class);

    private final TodoCrudService crudService;

    private final TodoSearchService searchService;

    @Autowired
    public TodoController(TodoCrudService crudService, TodoSearchService searchService) {
        this.crudService = crudService;
        this.searchService = searchService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public TodoDTO add(@RequestBody @Valid TodoDTO dto) {
        LOGGER.debug("Adding new todo entry with information: {}", dto);

        TodoDTO added = crudService.add(dto);

        LOGGER.info("Added todo entry: {}", added);

        return added;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public TodoDTO delete(@PathVariable("id") Long id) {
        LOGGER.info("Deleting todo entry with id: {}", id);

        TodoDTO deleted = crudService.delete(id);

        LOGGER.info("Deleted todo entry: {}", deleted);

        return deleted;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<TodoDTO> findAll() {
        LOGGER.info("Finding all todo entries");

        List<TodoDTO> todoEntries = crudService.findAll();

        LOGGER.info("Found {} todo entries.");

        return todoEntries;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TodoDTO findById(@PathVariable("id") Long id) {
        LOGGER.info("Finding todo entry with id: {}", id);

        TodoDTO found = crudService.findById(id);

        LOGGER.info("Found todo entry: {}", found);

        return found;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Page<TodoDTO> findBySearchTerm(@RequestParam("searchTerm") String searchTerm, Pageable pageable) {
        LOGGER.info("Finding {} todo entries for page {} by using search term: {}",
                pageable.getPageSize(),
                pageable.getPageNumber(),
                searchTerm
        );

        Page<TodoDTO> todoEntries = searchService.findBySearchTerm(searchTerm, pageable);

        LOGGER.info("Found {} todo entries for page: {}",
                todoEntries.getNumberOfElements(),
                todoEntries.getNumber()
        );

        return todoEntries;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public TodoDTO update(@PathVariable("id") Long id, @RequestBody @Valid TodoDTO dto) {
        dto.setId(id);

        LOGGER.info("Updating todo entry with information: {}", dto);

        TodoDTO updated = crudService.update(dto);

        LOGGER.info("Updated todo entry: {}", updated);

        return updated;
    }
}
