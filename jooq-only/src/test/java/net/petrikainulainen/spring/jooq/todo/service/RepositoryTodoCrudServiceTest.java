package net.petrikainulainen.spring.jooq.todo.service;

import net.petrikainulainen.spring.jooq.common.TestDateUtil;
import net.petrikainulainen.spring.jooq.config.ServiceTestContext;
import net.petrikainulainen.spring.jooq.todo.dto.TodoDTO;
import net.petrikainulainen.spring.jooq.todo.dto.TodoDTOBuilder;
import net.petrikainulainen.spring.jooq.todo.exception.TodoNotFoundException;
import net.petrikainulainen.spring.jooq.todo.model.Todo;
import net.petrikainulainen.spring.jooq.todo.repository.TodoRepository;
import org.jtransfo.JTransfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static net.petrikainulainen.spring.jooq.todo.dto.TodoDTOAssert.assertThatTodoDTO;
import static net.petrikainulainen.spring.jooq.todo.model.TodoAssert.assertThatTodo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Petri Kainulainen
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceTestContext.class})
public class RepositoryTodoCrudServiceTest {

    private static final String CREATION_TIME_STRING = TestDateUtil.CURRENT_TIMESTAMP;
    private static final Timestamp CREATION_TIME = TestDateUtil.parseTimestamp(CREATION_TIME_STRING);
    private static final String DESCRIPTION = "description";
    private static final Long ID = 1L;
    private static final String MODIFICATION_TIME_STRING = TestDateUtil.CURRENT_TIMESTAMP;
    private static final Timestamp MODIFICATION_TIME = TestDateUtil.parseTimestamp(MODIFICATION_TIME_STRING);
    private static final String TITLE = "title";

    @Mock
    private TodoRepository repositoryMock;

    private RepositoryTodoCrudService service;

    @Autowired
    private JTransfo transformer;

    @Before
    public void setUp() {
        initMocks(this);

        service = new RepositoryTodoCrudService(repositoryMock, transformer);
    }

    @Test
    public void add_NewTodoEntry_ShouldAddTodoEntryAndReturnAddedEntry() {
        TodoDTO newTodoEntry = new TodoDTOBuilder()
                .description(DESCRIPTION)
                .title(TITLE)
                .build();

        Todo addedTodoEntry = Todo.getBuilder(TITLE)
                .creationTime(CREATION_TIME)
                .description(DESCRIPTION)
                .id(ID)
                .modificationTime(MODIFICATION_TIME)
                .build();

        when(repositoryMock.add(isA(Todo.class))).thenReturn(addedTodoEntry);

        TodoDTO returnedTodoEntry = service.add(newTodoEntry);

        ArgumentCaptor<Todo> repositoryMethodArgument = ArgumentCaptor.forClass(Todo.class);

        verify(repositoryMock, times(1)).add(repositoryMethodArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        Todo repositoryMethodArgumentValue = repositoryMethodArgument.getValue();

        assertThatTodo(repositoryMethodArgumentValue)
                .creationTimeIsNotSet()
                .hasDescription(DESCRIPTION)
                .hasNoId()
                .hasTitle(TITLE)
                .modificationTimeIsNotSet();

        assertThatTodoDTO(returnedTodoEntry)
                .hasDescription(DESCRIPTION)
                .hasId(ID)
                .hasTitle(TITLE)
                .wasCreatedAt(CREATION_TIME_STRING)
                .wasModifiedAt(MODIFICATION_TIME_STRING);
    }

    @Test
    public void delete_TodoEntryNotFound_ShouldThrowException() {
        when(repositoryMock.delete(ID)).thenThrow(new TodoNotFoundException(""));

        catchException(service).delete(ID);
        assertThat(caughtException()).isExactlyInstanceOf(TodoNotFoundException.class);

        verify(repositoryMock, times(1)).delete(ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void delete_TodoEntryFound_ShouldReturnDeletedTodoEntry() {
        Todo deletedTodoEntry = Todo.getBuilder(TITLE)
                .creationTime(CREATION_TIME)
                .description(DESCRIPTION)
                .id(ID)
                .modificationTime(MODIFICATION_TIME)
                .build();

        when(repositoryMock.delete(ID)).thenReturn(deletedTodoEntry);

        TodoDTO returnedTodoEntry = service.delete(ID);

        verify(repositoryMock, times(1)).delete(ID);
        verifyNoMoreInteractions(repositoryMock);

        assertThatTodoDTO(returnedTodoEntry)
                .hasId(ID)
                .hasDescription(DESCRIPTION)
                .hasTitle(TITLE)
                .wasCreatedAt(CREATION_TIME_STRING)
                .wasModifiedAt(MODIFICATION_TIME_STRING);
    }

    @Test
    public void findAll_OneTodoEntryFound_ShouldReturnTheFoundTodoEntry() {
        Todo foundTodoEntry = Todo.getBuilder(TITLE)
                .creationTime(CREATION_TIME)
                .description(DESCRIPTION)
                .id(ID)
                .modificationTime(MODIFICATION_TIME)
                .build();

        when(repositoryMock.findAll()).thenReturn(Arrays.asList(foundTodoEntry));

        List<TodoDTO> returnedTodoEntries = service.findAll();

        verify(repositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(repositoryMock);

        assertThat(returnedTodoEntries).hasSize(1);

        TodoDTO returnedTodoEntry = returnedTodoEntries.get(0);
        assertThatTodoDTO(returnedTodoEntry)
                .hasDescription(DESCRIPTION)
                .hasId(ID)
                .hasTitle(TITLE)
                .wasCreatedAt(CREATION_TIME_STRING)
                .wasModifiedAt(MODIFICATION_TIME_STRING);
    }

    @Test
    public void findById_TodoEntryNotFound_ShouldThrowException() {
        when(repositoryMock.findById(ID)).thenThrow(new TodoNotFoundException(""));

        catchException(service).findById(ID);
        assertThat(caughtException()).isExactlyInstanceOf(TodoNotFoundException.class);

        verify(repositoryMock, times(1)).findById(ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void findById_TodoEntryFound_ShouldReturnFoundTodoEntry() {
        Todo foundTodoEntry = Todo.getBuilder(TITLE)
                .creationTime(CREATION_TIME)
                .description(DESCRIPTION)
                .id(ID)
                .modificationTime(MODIFICATION_TIME)
                .build();

        when(repositoryMock.findById(ID)).thenReturn(foundTodoEntry);

        TodoDTO returnedTodoEntry = service.findById(ID);

        verify(repositoryMock, times(1)).findById(ID);
        verifyNoMoreInteractions(repositoryMock);

        assertThatTodoDTO(returnedTodoEntry)
                .hasId(ID)
                .hasDescription(DESCRIPTION)
                .hasTitle(TITLE)
                .wasCreatedAt(CREATION_TIME_STRING)
                .wasModifiedAt(MODIFICATION_TIME_STRING);
    }

    @Test
    public void update_TodoEntryNotFound_ShouldThrowException() {
        TodoDTO updatedTodoEntry = new TodoDTOBuilder()
                .id(ID)
                .description(DESCRIPTION)
                .title(TITLE)
                .build();

        when(repositoryMock.update(isA(Todo.class))).thenThrow(new TodoNotFoundException(""));

        catchException(service).update(updatedTodoEntry);
        assertThat(caughtException()).isExactlyInstanceOf(TodoNotFoundException.class);

        ArgumentCaptor<Todo> repositoryMethodArgument = ArgumentCaptor.forClass(Todo.class);

        verify(repositoryMock, times(1)).update(repositoryMethodArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        Todo repositoryMethodArgumentValue = repositoryMethodArgument.getValue();

        assertThatTodo(repositoryMethodArgumentValue)
                .hasDescription(DESCRIPTION)
                .hasId(ID)
                .hasTitle(TITLE)
                .creationTimeIsNotSet()
                .modificationTimeIsNotSet();
    }

    @Test
    public void update_TodoEntryFound_ShouldUpdateTodoEntryAndReturnUpdatedTodoEntry() {
        TodoDTO existingTodoEntry = new TodoDTOBuilder()
                .id(ID)
                .description(DESCRIPTION)
                .title(TITLE)
                .build();

        Todo updatedTodoEntry = Todo.getBuilder(TITLE)
                .creationTime(CREATION_TIME)
                .description(DESCRIPTION)
                .id(ID)
                .modificationTime(MODIFICATION_TIME)
                .build();

        when(repositoryMock.update(isA(Todo.class))).thenReturn(updatedTodoEntry);

        TodoDTO returnedTodoEntry = service.update(existingTodoEntry);

        ArgumentCaptor<Todo> repositoryMethodArgument = ArgumentCaptor.forClass(Todo.class);

        verify(repositoryMock, times(1)).update(repositoryMethodArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        Todo repositoryMethodArgumentValue = repositoryMethodArgument.getValue();

        assertThatTodo(repositoryMethodArgumentValue)
                .hasDescription(DESCRIPTION)
                .hasId(ID)
                .hasTitle(TITLE)
                .creationTimeIsNotSet()
                .modificationTimeIsNotSet();

        assertThatTodoDTO(returnedTodoEntry)
                .hasDescription(DESCRIPTION)
                .hasId(ID)
                .hasTitle(TITLE)
                .wasCreatedAt(CREATION_TIME_STRING)
                .wasModifiedAt(MODIFICATION_TIME_STRING);
    }
}
