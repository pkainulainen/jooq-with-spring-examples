package net.petrikainulainen.spring.jooq.todo.service;

import net.petrikainulainen.spring.jooq.common.TestDateUtil;
import net.petrikainulainen.spring.jooq.config.ServiceTestContext;
import net.petrikainulainen.spring.jooq.todo.dto.TodoDTO;
import net.petrikainulainen.spring.jooq.todo.model.Todo;
import net.petrikainulainen.spring.jooq.todo.repository.TodoRepository;
import org.jtransfo.JTransfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.petrikainulainen.spring.jooq.todo.dto.TodoDTOAssert.assertThatTodoDTO;
import static org.assertj.core.api.Assertions.assertThat;
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
public class RepositoryTodoSearchServiceTest {

    private static final String CREATION_TIME_STRING = TestDateUtil.CURRENT_TIMESTAMP;
    private static final Timestamp CREATION_TIME = TestDateUtil.parseTimestamp(CREATION_TIME_STRING);
    private static final String DESCRIPTION = "description";
    private static final Long ID = 1L;
    private static final String MODIFICATION_TIME_STRING = TestDateUtil.CURRENT_TIMESTAMP;
    private static final Timestamp MODIFICATION_TIME = TestDateUtil.parseTimestamp(MODIFICATION_TIME_STRING);
    private static final String TITLE = "title";
    private static final String SEARCH_TERM = "title";

    @Mock
    private Pageable pageableMock;

    @Mock
    private TodoRepository repositoryMock;

    private RepositoryTodoSearchService service;

    @Autowired
    private JTransfo transformer;

    @Before
    public void setUp() {
        initMocks(this);

        service = new RepositoryTodoSearchService(repositoryMock, transformer);
    }

    @Test
    public void findBySearchTerm_NoTodoEntriesFound_ShouldReturnEmptyList() {
        when(repositoryMock.findBySearchTerm(SEARCH_TERM, pageableMock)).thenReturn(new ArrayList<Todo>());

        List<TodoDTO> searchResults = service.findBySearchTerm(SEARCH_TERM, pageableMock);

        verify(repositoryMock, times(1)).findBySearchTerm(SEARCH_TERM, pageableMock);
        verifyNoMoreInteractions(repositoryMock);

        assertThat(searchResults).isEmpty();
    }

    @Test
    public void findBySearchTerm_OneTodoEntryFound_ShouldReturnFoundTodoEntry() {
        Todo expectedTodoEntry = Todo.getBuilder(TITLE)
                .creationTime(CREATION_TIME)
                .description(DESCRIPTION)
                .id(ID)
                .modificationTime(MODIFICATION_TIME)
                .build();

        when(repositoryMock.findBySearchTerm(SEARCH_TERM, pageableMock)).thenReturn(Arrays.asList(expectedTodoEntry));

        List<TodoDTO> searchResults = service.findBySearchTerm(SEARCH_TERM, pageableMock);

        verify(repositoryMock, times(1)).findBySearchTerm(SEARCH_TERM, pageableMock);
        verifyNoMoreInteractions(repositoryMock);

        assertThat(searchResults).hasSize(1);

        TodoDTO found = searchResults.get(0);
        assertThatTodoDTO(found)
                .hasDescription(DESCRIPTION)
                .hasId(ID)
                .hasTitle(TITLE)
                .wasCreatedAt(CREATION_TIME_STRING)
                .wasModifiedAt(MODIFICATION_TIME_STRING);
    }
}
