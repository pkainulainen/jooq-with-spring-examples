package net.petrikainulainen.spring.jooq.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import net.petrikainulainen.spring.jooq.WebTestConstants;
import net.petrikainulainen.spring.jooq.WebTestUtil;
import net.petrikainulainen.spring.jooq.config.ExampleApplicationContext;
import net.petrikainulainen.spring.jooq.todo.IntegrationTestConstants;
import net.petrikainulainen.spring.jooq.todo.dto.TodoDTO;
import net.petrikainulainen.spring.jooq.todo.dto.TodoDTOBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Petri Kainulainen
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ExampleApplicationContext.class})
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
public class ITTodoControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webAppContext;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    public void add_EmptyTodoEntry_ShouldReturnValidationErrorAboutMissingTitleAsJsonDocument() throws Exception {
        TodoDTO addedTodoEntry = new TodoDTO();

        mockMvc.perform(post("/api/todo")
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(addedTodoEntry))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.validationErrors", hasSize(1)))
                .andExpect(jsonPath("$.validationErrors[?(@.field=='title')].errorCode", contains(WebTestConstants.ERROR_CODE_NOT_EMPTY)))
                .andExpect(jsonPath("$.validationErrors[?(@.field=='title')].errorMessage", not(isEmptyOrNullString())));
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    public void add_TitleAndDescriptionAreTooLong_ShouldReturnValidationErrorsAboutTitleAndDescriptionAsJsonDocument() throws Exception {
        String tooLongTitle = WebTestUtil.createStringWithLength(101);
        String tooLongDescription = WebTestUtil.createStringWithLength(501);

        TodoDTO addedTodoEntry = new TodoDTOBuilder()
                .description(tooLongDescription)
                .title(tooLongTitle)
                .build();

        mockMvc.perform(post("/api/todo")
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(addedTodoEntry))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.validationErrors", hasSize(2)))
                .andExpect(jsonPath("$.validationErrors[?(@.field=='description')].errorCode", contains(WebTestConstants.ERROR_CODE_LENGTH)))
                .andExpect(jsonPath("$.validationErrors[?(@.field=='description')].errorMessage", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.validationErrors[?(@.field=='title')].errorCode", contains(WebTestConstants.ERROR_CODE_LENGTH)))
                .andExpect(jsonPath("$.validationErrors[?(@.field=='title')].errorMessage", not(isEmptyOrNullString())));
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    @ExpectedDatabase(value="/net/petrikainulainen/spring/jooq/todo/todo-data-add.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add_TodoEntryAdded_ShouldReturnAddedToEntryAsJsonDocument() throws Exception {
        TodoDTO addedTodoEntry = new TodoDTOBuilder()
                .description(IntegrationTestConstants.NEW_DESCRIPTION)
                .title(IntegrationTestConstants.NEW_TITLE)
                .build();

        mockMvc.perform(post("/api/todo")
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(addedTodoEntry))
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", isA(Integer.class)))
                .andExpect(jsonPath("$.creationTime", is(IntegrationTestConstants.NEW_CREATION_TIME)))
                .andExpect(jsonPath("$.description", is(IntegrationTestConstants.NEW_DESCRIPTION)))
                .andExpect(jsonPath("$.modificationTime", is(IntegrationTestConstants.NEW_MODIFICATION_TIME)))
                .andExpect(jsonPath("$.title", is(IntegrationTestConstants.NEW_TITLE)));
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    public void delete_TodoEntryNotFound_ShouldReturnHttpStatusCodeNotFound() throws Exception {
        mockMvc.perform(delete("/api/todo/{id}", IntegrationTestConstants.ID_FIRST_TODO))
                .andExpect(status().isNotFound());
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/todo-data-deleted.xml")
    public void delete_TodoEntryFound_ShouldReturnDeletedTodoEntryAsJsonDocument() throws Exception {
        mockMvc.perform(delete("/api/todo/{id}", IntegrationTestConstants.ID_FIRST_TODO))
                .andExpect(status().isOk())
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(IntegrationTestConstants.ID_FIRST_TODO.intValue())))
                .andExpect(jsonPath("$.creationTime", is(IntegrationTestConstants.CURRENT_CREATION_TIME)))
                .andExpect(jsonPath("$.description", is(IntegrationTestConstants.CURRENT_DESCRIPTION)))
                .andExpect(jsonPath("$.modificationTime", is(IntegrationTestConstants.CURRENT_MODIFICATION_TIME)))
                .andExpect(jsonPath("$.title", is(IntegrationTestConstants.CURRENT_TITLE_FIRST_TODO)));
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    public void findAll_NoTodoEntriesFound_ShouldReturnEmptyListAsJsonDocument() throws Exception {
        mockMvc.perform(get("/api/todo"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    public void findAll_TwoTodoEntriesFound_ShouldReturnTodoEntriesAsJsonDocument() throws Exception {
        mockMvc.perform(get("/api/todo"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(IntegrationTestConstants.ID_FIRST_TODO.intValue())))
                .andExpect(jsonPath("$[0].creationTime", is(IntegrationTestConstants.CURRENT_CREATION_TIME)))
                .andExpect(jsonPath("$[0].description", is(IntegrationTestConstants.CURRENT_DESCRIPTION)))
                .andExpect(jsonPath("$[0].modificationTime", is(IntegrationTestConstants.CURRENT_MODIFICATION_TIME)))
                .andExpect(jsonPath("$[0].title", is(IntegrationTestConstants.CURRENT_TITLE_FIRST_TODO)))
                .andExpect(jsonPath("$[1].id", is(IntegrationTestConstants.ID_SECOND_TODO.intValue())))
                .andExpect(jsonPath("$[1].creationTime", is(IntegrationTestConstants.CURRENT_CREATION_TIME)))
                .andExpect(jsonPath("$[1].description", is(IntegrationTestConstants.CURRENT_DESCRIPTION)))
                .andExpect(jsonPath("$[1].modificationTime", is(IntegrationTestConstants.CURRENT_MODIFICATION_TIME)))
                .andExpect(jsonPath("$[1].title", is(IntegrationTestConstants.CURRENT_TITLE_SECOND_TODO)));
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    public void findById_TodoEntryFound_ShouldReturnTodoEntryAsJsonDocument() throws Exception {
        mockMvc.perform(get("/api/todo/{id}", IntegrationTestConstants.ID_FIRST_TODO))
                .andExpect(status().isOk())
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(IntegrationTestConstants.ID_FIRST_TODO.intValue())))
                .andExpect(jsonPath("$.creationTime", is(IntegrationTestConstants.CURRENT_CREATION_TIME)))
                .andExpect(jsonPath("$.description", is(IntegrationTestConstants.CURRENT_DESCRIPTION)))
                .andExpect(jsonPath("$.modificationTime", is(IntegrationTestConstants.CURRENT_MODIFICATION_TIME)))
                .andExpect(jsonPath("$.title", is(IntegrationTestConstants.CURRENT_TITLE_FIRST_TODO)));
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    public void findById_TodoEntryNotFound_ShouldReturnHttpStatusNotFound() throws Exception {
        mockMvc.perform(get("/api/todo/{id}", IntegrationTestConstants.ID_FIRST_TODO))
                .andExpect(status().isNotFound());
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    public void findBySearchTerm_NoTodoEntriesFound_ShouldReturnEmptyListAsJsonDocument() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                .param("searchTerm", IntegrationTestConstants.SEARCH_TERM)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    public void findBySearchTerm_OneTodoEntryFound_ShouldReturnTodoEntriesAsJsonDocument() throws Exception {
        mockMvc.perform(get("/api/todo/search")
                .param("searchTerm", IntegrationTestConstants.SEARCH_TERM)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(IntegrationTestConstants.ID_FIRST_TODO.intValue())))
                .andExpect(jsonPath("$[0].creationTime", is(IntegrationTestConstants.CURRENT_CREATION_TIME)))
                .andExpect(jsonPath("$[0].description", is(IntegrationTestConstants.CURRENT_DESCRIPTION)))
                .andExpect(jsonPath("$[0].modificationTime", is(IntegrationTestConstants.CURRENT_MODIFICATION_TIME)))
                .andExpect(jsonPath("$[0].title", is(IntegrationTestConstants.CURRENT_TITLE_FIRST_TODO)));
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    public void update_EmptyTodoEntry_ShouldReturnValidationErrorAboutMissingTitleAsJsonDocument() throws Exception {
        TodoDTO updatedTodoEntry = new TodoDTO();

        mockMvc.perform(put("/api/todo/{id}", IntegrationTestConstants.ID_SECOND_TODO)
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(updatedTodoEntry))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.validationErrors", hasSize(1)))
                .andExpect(jsonPath("$.validationErrors[?(@.field=='title')].errorCode", contains(WebTestConstants.ERROR_CODE_NOT_EMPTY)))
                .andExpect(jsonPath("$.validationErrors[?(@.field=='title')].errorMessage", not(isEmptyOrNullString())));
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    public void update_TitleAndDescriptionAreTooLong_ShouldReturnValidationErrorsAboutTitleAndDescriptionAsJsonDocument() throws Exception {
        String tooLongTitle = WebTestUtil.createStringWithLength(101);
        String tooLongDescription = WebTestUtil.createStringWithLength(501);

        TodoDTO updatedTodoEntry = new TodoDTOBuilder()
                .description(tooLongDescription)
                .title(tooLongTitle)
                .build();

        mockMvc.perform(put("/api/todo/{id}", IntegrationTestConstants.ID_SECOND_TODO)
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(updatedTodoEntry))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.validationErrors", hasSize(2)))
                .andExpect(jsonPath("$.validationErrors[?(@.field=='description')].errorCode", contains(WebTestConstants.ERROR_CODE_LENGTH)))
                .andExpect(jsonPath("$.validationErrors[?(@.field=='description')].errorMessage", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.validationErrors[?(@.field=='title')].errorCode", contains(WebTestConstants.ERROR_CODE_LENGTH)))
                .andExpect(jsonPath("$.validationErrors[?(@.field=='title')].errorMessage", not(isEmptyOrNullString())));
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    public void update_TodoEntryNotFound_ShouldReturnHttpStatusCodeNotFound() throws Exception {
        TodoDTO updatedTodoEntry = new TodoDTOBuilder()
                .description(IntegrationTestConstants.NEW_DESCRIPTION)
                .title(IntegrationTestConstants.NEW_TITLE)
                .build();

        mockMvc.perform(put("/api/todo/{id}", IntegrationTestConstants.ID_SECOND_TODO)
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(updatedTodoEntry))
        )
                .andExpect(status().isNotFound());
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/todo-data-updated.xml")
    public void update_TodoEntryFound_ShouldReturnUpdatedTodoEntryAsJsonDocument() throws Exception {
        TodoDTO updatedTodoEntry = new TodoDTOBuilder()
                .description(IntegrationTestConstants.NEW_DESCRIPTION)
                .title(IntegrationTestConstants.NEW_TITLE)
                .build();

        mockMvc.perform(put("/api/todo/{id}", IntegrationTestConstants.ID_SECOND_TODO)
                        .contentType(WebTestConstants.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(updatedTodoEntry))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(WebTestConstants.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(IntegrationTestConstants.ID_SECOND_TODO.intValue())))
                .andExpect(jsonPath("$.creationTime", is(IntegrationTestConstants.CURRENT_CREATION_TIME)))
                .andExpect(jsonPath("$.description", is(IntegrationTestConstants.NEW_DESCRIPTION)))
                .andExpect(jsonPath("$.modificationTime", is(IntegrationTestConstants.NEW_MODIFICATION_TIME)))
                .andExpect(jsonPath("$.title", is(IntegrationTestConstants.NEW_TITLE)));
    }
}
