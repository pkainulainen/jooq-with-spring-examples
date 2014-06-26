package net.petrikainulainen.spring.jooq.todo.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import net.petrikainulainen.spring.jooq.config.PersistenceContext;
import net.petrikainulainen.spring.jooq.todo.IntegrationTestConstants;
import net.petrikainulainen.spring.jooq.todo.exception.TodoNotFoundException;
import net.petrikainulainen.spring.jooq.todo.model.Todo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static net.petrikainulainen.spring.jooq.todo.PageAssert.assertThatPage;
import static net.petrikainulainen.spring.jooq.todo.model.TodoAssert.assertThatTodo;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petri Kainulainen
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceContext.class})
//@ContextConfiguration(locations = {"classpath:exampleApplicationContext-persistence.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
public class ITJOOQTodoRepositoryTest {

    private static final int FIRST_PAGE = 0;
    private static final int SECOND_PAGE = 1;
    private static final int THIRD_PAGE = 2;
    private static final int PAGE_SIZE = 1;
    private static final int ONE_ELEMENT_ON_PAGE = 1;
    private static final long TWO_ELEMENTS = 2;
    private static final int TWO_PAGES = 2;
    private static final long ZERO_ELEMENTS = 0L;
    private static final int ZERO_ELEMENTS_ON_PAGE = 0;
    private static final int ZERO_PAGES = 0;


    @Autowired
    private TodoRepository repository;

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    @ExpectedDatabase(value="/net/petrikainulainen/spring/jooq/todo/todo-data-add.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add_ShouldAddNewTodoEntry() {
        Todo newTodoEntry = Todo.getBuilder(IntegrationTestConstants.NEW_TITLE)
                .description(IntegrationTestConstants.NEW_DESCRIPTION)
                .build();

        Todo persistedTodoEntry = repository.add(newTodoEntry);

        assertThatTodo(persistedTodoEntry)
                .hasId()
                .hasDescription(IntegrationTestConstants.NEW_DESCRIPTION)
                .hasTitle(IntegrationTestConstants.NEW_TITLE)
                .wasCreatedAt(IntegrationTestConstants.NEW_CREATION_TIME)
                .wasModifiedAt(IntegrationTestConstants.NEW_MODIFICATION_TIME);
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    public void delete_TodoEntryNotFound_ShouldDeleteTodo() {
        catchException(repository).delete(IntegrationTestConstants.ID_FIRST_TODO);
        assertThat(caughtException()).isExactlyInstanceOf(TodoNotFoundException.class);
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/todo-data-deleted.xml")
    public void delete_TodoEntryFound_ShouldDeleteTodo() {
        Todo deletedTodoEntry = repository.delete(IntegrationTestConstants.ID_FIRST_TODO);

        assertThatTodo(deletedTodoEntry)
                .hasId(IntegrationTestConstants.ID_FIRST_TODO)
                .hasDescription(IntegrationTestConstants.CURRENT_DESCRIPTION)
                .hasTitle(IntegrationTestConstants.CURRENT_TITLE_FIRST_TODO)
                .wasCreatedAt(IntegrationTestConstants.CURRENT_CREATION_TIME)
                .wasModifiedAt(IntegrationTestConstants.CURRENT_MODIFICATION_TIME);
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    public void findAll_NoTodoEntriesFound_ShouldReturnEmptyList() {
        List<Todo> todoEntries = repository.findAll();
        assertThat(todoEntries).isEmpty();
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    public void findAll_TwoTodoEntriesFound_ShouldReturnTwoTodoEntries() {
        List<Todo> todoEntries = repository.findAll();

        assertThat(todoEntries).hasSize(2);

        Todo firstTodoEntry = todoEntries.get(0);
        assertThatTodo(firstTodoEntry)
                .hasId(IntegrationTestConstants.ID_FIRST_TODO)
                .hasDescription(IntegrationTestConstants.CURRENT_DESCRIPTION)
                .hasTitle(IntegrationTestConstants.CURRENT_TITLE_FIRST_TODO)
                .wasCreatedAt(IntegrationTestConstants.CURRENT_CREATION_TIME)
                .wasModifiedAt(IntegrationTestConstants.CURRENT_MODIFICATION_TIME);

        Todo secondTodoEntry = todoEntries.get(1);
        assertThatTodo(secondTodoEntry)
                .hasId(IntegrationTestConstants.ID_SECOND_TODO)
                .hasDescription(IntegrationTestConstants.CURRENT_DESCRIPTION)
                .hasTitle(IntegrationTestConstants.CURRENT_TITLE_SECOND_TODO)
                .wasCreatedAt(IntegrationTestConstants.CURRENT_CREATION_TIME)
                .wasModifiedAt(IntegrationTestConstants.CURRENT_MODIFICATION_TIME);
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    public void findById_TodoEntryFound_ShouldReturnTodo() {
        Todo foundTodoEntry = repository.findById(1L);

        assertThatTodo(foundTodoEntry)
                .hasId(IntegrationTestConstants.ID_FIRST_TODO)
                .hasDescription(IntegrationTestConstants.CURRENT_DESCRIPTION)
                .hasTitle(IntegrationTestConstants.CURRENT_TITLE_FIRST_TODO)
                .wasCreatedAt(IntegrationTestConstants.CURRENT_CREATION_TIME)
                .wasModifiedAt(IntegrationTestConstants.CURRENT_MODIFICATION_TIME);
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    public void findById_TodoEntryNotFound_ShouldThrowException() {
        catchException(repository).findById(IntegrationTestConstants.ID_FIRST_TODO);
        assertThat(caughtException()).isExactlyInstanceOf(TodoNotFoundException.class);
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    public void findBySearchTerm_TodoEntriesNotFound_ShouldReturnPageWithoutElements() {
        Page<Todo> firstPage = repository.findBySearchTerm(IntegrationTestConstants.SEARCH_TERM,
                new PageRequest(FIRST_PAGE, PAGE_SIZE)
        );

        assertThatPage(firstPage)
                .hasPageNumber(FIRST_PAGE)
                .hasPageSize(PAGE_SIZE)
                .isEmpty()
                .isFirstPage()
                .isLastPage()
                .hasTotalNumberOfElements(ZERO_ELEMENTS)
                .hasTotalNumberOfPages(ZERO_PAGES);

    }


    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    public void findBySearchTerm_FirstPageWithPageSizeOne_TwoTodoEntriesExist_ShouldReturnFirstPageWithFirstTodoEntry() {
        Page<Todo> firstPage = repository.findBySearchTerm(IntegrationTestConstants.SEARCH_TERM,
                new PageRequest(FIRST_PAGE, PAGE_SIZE)
        );

        assertThatPage(firstPage)
                .hasPageNumber(FIRST_PAGE)
                .hasPageSize(PAGE_SIZE)
                .hasNumberOfElements(ONE_ELEMENT_ON_PAGE)
                .isFirstPage()
                .isNotLastPage()
                .hasTotalNumberOfElements(TWO_ELEMENTS)
                .hasTotalNumberOfPages(TWO_PAGES);

        Todo foundTodoEntry = firstPage.getContent().get(0);
        assertThatTodo(foundTodoEntry)
                .hasId(IntegrationTestConstants.ID_FIRST_TODO)
                .hasDescription(IntegrationTestConstants.CURRENT_DESCRIPTION)
                .hasTitle(IntegrationTestConstants.CURRENT_TITLE_FIRST_TODO)
                .wasCreatedAt(IntegrationTestConstants.CURRENT_CREATION_TIME)
                .wasModifiedAt(IntegrationTestConstants.CURRENT_MODIFICATION_TIME);
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    public void findBySearchTerm_FirstPageWithPageSizeOne_TwoTodoEntriesExistAndSortedByTitleAsc_ShouldReturnFirstPageWithSecondTodoEntry() {
        Sort sortSpecification = new Sort(new Sort.Order(Sort.Direction.ASC, IntegrationTestConstants.SORT_FIELD_NAME));
        PageRequest pageSpecification = new PageRequest(FIRST_PAGE, PAGE_SIZE, sortSpecification);

        Page<Todo> firstPage = repository.findBySearchTerm(IntegrationTestConstants.SEARCH_TERM, pageSpecification);

        assertThatPage(firstPage)
                .hasPageNumber(FIRST_PAGE)
                .hasPageSize(PAGE_SIZE)
                .hasNumberOfElements(ONE_ELEMENT_ON_PAGE)
                .isFirstPage()
                .isNotLastPage()
                .hasTotalNumberOfElements(TWO_ELEMENTS)
                .hasTotalNumberOfPages(TWO_PAGES);

        Todo foundTodoEntry = firstPage.getContent().get(0);
        assertThatTodo(foundTodoEntry)
                .hasId(IntegrationTestConstants.ID_SECOND_TODO)
                .hasDescription(IntegrationTestConstants.CURRENT_DESCRIPTION)
                .hasTitle(IntegrationTestConstants.CURRENT_TITLE_SECOND_TODO)
                .wasCreatedAt(IntegrationTestConstants.CURRENT_CREATION_TIME)
                .wasModifiedAt(IntegrationTestConstants.CURRENT_MODIFICATION_TIME);
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    public void findBySearchTerm_FirstPageWithPageSizeOne_TwoTodoEntriesExistAndSortedByTitleDesc_ShouldReturnFirstPageWithFirstTodoEntry() {
        Sort sortSpecification = new Sort(new Sort.Order(Sort.Direction.DESC, IntegrationTestConstants.SORT_FIELD_NAME));
        PageRequest pageSpecification = new PageRequest(FIRST_PAGE, PAGE_SIZE, sortSpecification);

        Page<Todo> firstPage = repository.findBySearchTerm(IntegrationTestConstants.SEARCH_TERM, pageSpecification);

        assertThatPage(firstPage)
                .hasPageNumber(FIRST_PAGE)
                .hasPageSize(PAGE_SIZE)
                .hasNumberOfElements(ONE_ELEMENT_ON_PAGE)
                .isFirstPage()
                .isNotLastPage()
                .hasTotalNumberOfElements(TWO_ELEMENTS)
                .hasTotalNumberOfPages(TWO_PAGES);

        Todo foundTodoEntry = firstPage.getContent().get(0);
        assertThatTodo(foundTodoEntry)
                .hasId(IntegrationTestConstants.ID_FIRST_TODO)
                .hasDescription(IntegrationTestConstants.CURRENT_DESCRIPTION)
                .hasTitle(IntegrationTestConstants.CURRENT_TITLE_FIRST_TODO)
                .wasCreatedAt(IntegrationTestConstants.CURRENT_CREATION_TIME)
                .wasModifiedAt(IntegrationTestConstants.CURRENT_MODIFICATION_TIME);
    }


    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    public void findBySearchTerm_SecondPageWithPageSizeOne_TwoTodoEntriesExist_ShouldReturnSecondPageWithSecondTodoEntry() {
        Page<Todo> secondPage = repository.findBySearchTerm(IntegrationTestConstants.SEARCH_TERM,
                new PageRequest(SECOND_PAGE, PAGE_SIZE)
        );

        assertThatPage(secondPage)
                .hasPageNumber(SECOND_PAGE)
                .hasPageSize(PAGE_SIZE)
                .hasNumberOfElements(ONE_ELEMENT_ON_PAGE)
                .isNotFirstPage()
                .isLastPage()
                .hasTotalNumberOfElements(TWO_ELEMENTS)
                .hasTotalNumberOfPages(TWO_PAGES);

        Todo foundTodoEntry = secondPage.getContent().get(0);
        assertThatTodo(foundTodoEntry)
                .hasId(IntegrationTestConstants.ID_SECOND_TODO)
                .hasDescription(IntegrationTestConstants.CURRENT_DESCRIPTION)
                .hasTitle(IntegrationTestConstants.CURRENT_TITLE_SECOND_TODO)
                .wasCreatedAt(IntegrationTestConstants.CURRENT_CREATION_TIME)
                .wasModifiedAt(IntegrationTestConstants.CURRENT_MODIFICATION_TIME);
    }


    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    public void findBySearchTerm_SecondPageWithPageSizeOne_TwoTodoEntriesExistAndSortedByTitleAsc_ShouldReturnSecondPageWithFirstTodoEntry() {
        Sort sortSpecification = new Sort(new Sort.Order(Sort.Direction.ASC, IntegrationTestConstants.SORT_FIELD_NAME));
        PageRequest pageSpecification = new PageRequest(SECOND_PAGE, PAGE_SIZE, sortSpecification);

        Page<Todo> secondPage = repository.findBySearchTerm(IntegrationTestConstants.SEARCH_TERM, pageSpecification);

        assertThatPage(secondPage)
                .hasPageNumber(SECOND_PAGE)
                .hasPageSize(PAGE_SIZE)
                .hasNumberOfElements(ONE_ELEMENT_ON_PAGE)
                .isNotFirstPage()
                .isLastPage()
                .hasTotalNumberOfElements(TWO_ELEMENTS)
                .hasTotalNumberOfPages(TWO_PAGES);

        Todo foundTodoEntry = secondPage.getContent().get(0);
        assertThatTodo(foundTodoEntry)
                .hasId(IntegrationTestConstants.ID_FIRST_TODO)
                .hasDescription(IntegrationTestConstants.CURRENT_DESCRIPTION)
                .hasTitle(IntegrationTestConstants.CURRENT_TITLE_FIRST_TODO)
                .wasCreatedAt(IntegrationTestConstants.CURRENT_CREATION_TIME)
                .wasModifiedAt(IntegrationTestConstants.CURRENT_MODIFICATION_TIME);
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    public void findBySearchTerm_SecondPageWithPageSizeOne_TwoTodoEntriesExistAndSortedByTitleDesc_ShouldReturnSecondPageWithSecondTodoEntry() {
        Sort sortSpecification = new Sort(new Sort.Order(Sort.Direction.DESC, IntegrationTestConstants.SORT_FIELD_NAME));
        PageRequest pageSpecification = new PageRequest(1, 1, sortSpecification);

        Page<Todo> secondPage = repository.findBySearchTerm(IntegrationTestConstants.SEARCH_TERM, pageSpecification);

        assertThatPage(secondPage)
                .hasPageNumber(SECOND_PAGE)
                .hasPageSize(PAGE_SIZE)
                .hasNumberOfElements(ONE_ELEMENT_ON_PAGE)
                .isNotFirstPage()
                .isLastPage()
                .hasTotalNumberOfElements(TWO_ELEMENTS)
                .hasTotalNumberOfPages(TWO_PAGES);

        Todo foundTodoEntry = secondPage.getContent().get(0);
        assertThatTodo(foundTodoEntry)
                .hasId(IntegrationTestConstants.ID_SECOND_TODO)
                .hasDescription(IntegrationTestConstants.CURRENT_DESCRIPTION)
                .hasTitle(IntegrationTestConstants.CURRENT_TITLE_SECOND_TODO)
                .wasCreatedAt(IntegrationTestConstants.CURRENT_CREATION_TIME)
                .wasModifiedAt(IntegrationTestConstants.CURRENT_MODIFICATION_TIME);
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    public void findBySearchTerm_ThirdPageWithPageSizeOne_TwoTodoEntriesExist_ShouldReturnPageWithEmptyList() {
        Page<Todo> thirdPage = repository.findBySearchTerm(IntegrationTestConstants.SEARCH_TERM, new PageRequest(2, 1));

        assertThatPage(thirdPage)
                .hasPageNumber(THIRD_PAGE)
                .hasPageSize(PAGE_SIZE)
                .hasNumberOfElements(ZERO_ELEMENTS_ON_PAGE)
                .isNotFirstPage()
                .isLastPage()
                .hasTotalNumberOfElements(TWO_ELEMENTS)
                .hasTotalNumberOfPages(TWO_PAGES);
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    public void findBySearchTerm_ThirdPageWithPageSizeOne_TwoTodoEntriesExistAndSortedByTitleAsc_ShouldReturnPageWithEmptyList() {
        Sort sortSpecification = new Sort(new Sort.Order(Sort.Direction.ASC, IntegrationTestConstants.SORT_FIELD_NAME));
        PageRequest pageSpecification = new PageRequest(2, 1, sortSpecification);

        Page<Todo> thirdPage = repository.findBySearchTerm(IntegrationTestConstants.SEARCH_TERM, pageSpecification);

        assertThatPage(thirdPage)
                .hasPageNumber(THIRD_PAGE)
                .hasPageSize(PAGE_SIZE)
                .hasNumberOfElements(ZERO_ELEMENTS_ON_PAGE)
                .isNotFirstPage()
                .isLastPage()
                .hasTotalNumberOfElements(TWO_ELEMENTS)
                .hasTotalNumberOfPages(TWO_PAGES);
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    public void findBySearchTerm_ThirdPageWithPageSizeOne_TwoTodoEntriesExistAndSortedByTitleDesc_ShouldReturnPageWithEmptyList() {
        Sort sortSpecification = new Sort(new Sort.Order(Sort.Direction.DESC, IntegrationTestConstants.SORT_FIELD_NAME));
        PageRequest pageSpecification = new PageRequest(2, 1, sortSpecification);

        Page<Todo> thirdPage = repository.findBySearchTerm(IntegrationTestConstants.SEARCH_TERM, pageSpecification);

        assertThatPage(thirdPage)
                .hasPageNumber(THIRD_PAGE)
                .hasPageSize(PAGE_SIZE)
                .hasNumberOfElements(ZERO_ELEMENTS_ON_PAGE)
                .isNotFirstPage()
                .isLastPage()
                .hasTotalNumberOfElements(TWO_ELEMENTS)
                .hasTotalNumberOfPages(TWO_PAGES);
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    @ExpectedDatabase("/net/petrikainulainen/spring/jooq/todo/empty-todo-data.xml")
    public void update_TodoEntryNotFound_ShouldThrowException() {
        Todo updatedTodoEntry = Todo.getBuilder("title")
                .description("description")
                .id(IntegrationTestConstants.ID_SECOND_TODO)
                .build();

        catchException(repository).update(updatedTodoEntry);
        assertThat(caughtException()).isExactlyInstanceOf(TodoNotFoundException.class);
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/jooq/todo/todo-data.xml")
    @ExpectedDatabase(value="/net/petrikainulainen/spring/jooq/todo/todo-data-updated.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void update_TodoEntryFound_ShouldUpdateTodo() {
        Todo updatedTodoEntry = Todo.getBuilder(IntegrationTestConstants.NEW_TITLE)
                .description(IntegrationTestConstants.NEW_DESCRIPTION)
                .id(IntegrationTestConstants.ID_SECOND_TODO)
                .build();

        Todo returnedTodoEntry = repository.update(updatedTodoEntry);

        assertThatTodo(returnedTodoEntry)
                .hasId(IntegrationTestConstants.ID_SECOND_TODO)
                .hasDescription(IntegrationTestConstants.NEW_DESCRIPTION)
                .hasTitle(IntegrationTestConstants.NEW_TITLE)
                .wasCreatedAt(IntegrationTestConstants.CURRENT_CREATION_TIME)
                .wasModifiedAt(IntegrationTestConstants.NEW_MODIFICATION_TIME);
    }
}
