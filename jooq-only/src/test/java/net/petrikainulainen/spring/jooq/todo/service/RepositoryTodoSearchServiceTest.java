package net.petrikainulainen.spring.jooq.todo.service;

import net.petrikainulainen.spring.jooq.common.TestDateUtil;
import net.petrikainulainen.spring.jooq.config.ServiceTestContext;
import net.petrikainulainen.spring.jooq.todo.PageBuilder;
import net.petrikainulainen.spring.jooq.todo.dto.TodoDTO;
import net.petrikainulainen.spring.jooq.todo.model.Todo;
import net.petrikainulainen.spring.jooq.todo.repository.TodoRepository;
import org.jtransfo.JTransfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;

import static net.petrikainulainen.spring.jooq.todo.PageAssert.assertThatPage;
import static net.petrikainulainen.spring.jooq.todo.dto.TodoDTOAssert.assertThatTodoDTO;
import static org.mockito.Mockito.mock;
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

    private static final int PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = 10;
    private static final Sort SORT = mock(Sort.class);

    private static final int ONE_ELEMENT_ON_PAGE = 1;
    private static final long ONE_ELEMENT = 1L;
    private static final int ONE_PAGE = 1;
    private static final long ZERO_ELEMENTS = 0L;
    private static final int ZERO_PAGES = 0;

    @Mock
    private Pageable pageRequest;

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
        Page<Todo> page = new PageBuilder<Todo>()
                .pageNumber(PAGE_NUMBER)
                .pageSize(PAGE_SIZE)
                .sort(SORT)
                .build();

        when(repositoryMock.findBySearchTerm(SEARCH_TERM, pageRequest)).thenReturn(page);

        Page<TodoDTO> searchResults = service.findBySearchTerm(SEARCH_TERM, pageRequest);

        verify(repositoryMock, times(1)).findBySearchTerm(SEARCH_TERM, pageRequest);
        verifyNoMoreInteractions(repositoryMock);

        assertThatPage(searchResults)
                .isFirstPage()
                .isLastPage()
                .hasPageNumber(PAGE_NUMBER)
                .hasPageSize(PAGE_SIZE)
                .hasSort(SORT)
                .hasTotalNumberOfElements(ZERO_ELEMENTS)
                .hasTotalNumberOfPages(ZERO_PAGES)
                .isEmpty();
    }

    @Test
    public void findBySearchTerm_OneTodoEntryFound_ShouldReturnFoundTodoEntry() {
        Todo expectedTodoEntry = Todo.getBuilder(TITLE)
                .creationTime(CREATION_TIME)
                .description(DESCRIPTION)
                .id(ID)
                .modificationTime(MODIFICATION_TIME)
                .build();

        Page<Todo> page = new PageBuilder<Todo>()
                .itemsOnPage(expectedTodoEntry)
                .pageNumber(PAGE_NUMBER)
                .pageSize(PAGE_SIZE)
                .totalNumberOfItems(ONE_ELEMENT)
                .sort(SORT)
                .build();

        when(repositoryMock.findBySearchTerm(SEARCH_TERM, pageRequest)).thenReturn(page);

        Page<TodoDTO> searchResults = service.findBySearchTerm(SEARCH_TERM, pageRequest);

        verify(repositoryMock, times(1)).findBySearchTerm(SEARCH_TERM, pageRequest);
        verifyNoMoreInteractions(repositoryMock);

        assertThatPage(searchResults)
                .isFirstPage()
                .isLastPage()
                .hasPageNumber(PAGE_NUMBER)
                .hasPageSize(PAGE_SIZE)
                .hasSort(SORT)
                .hasNumberOfElements(ONE_ELEMENT_ON_PAGE)
                .hasTotalNumberOfElements(ONE_ELEMENT)
                .hasTotalNumberOfPages(ONE_PAGE);

        TodoDTO found = searchResults.getContent().get(0);
        assertThatTodoDTO(found)
                .hasDescription(DESCRIPTION)
                .hasId(ID)
                .hasTitle(TITLE)
                .wasCreatedAt(CREATION_TIME_STRING)
                .wasModifiedAt(MODIFICATION_TIME_STRING);
    }
}
