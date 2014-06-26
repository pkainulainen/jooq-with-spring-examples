package net.petrikainulainen.spring.jooq.todo;

import org.assertj.core.api.AbstractAssert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petri Kainulainen
 */
public class PageAssert extends AbstractAssert<PageAssert, Page> {

    private PageAssert(Page actual) {
        super(actual, PageAssert.class);
    }

    public static PageAssert assertThatPage(Page page) {
        return new PageAssert(page);
    }

    public PageAssert hasNumberOfElements(int numberOfElements) {
        isNotNull();

        assertThat(actual.getNumberOfElements())
                .overridingErrorMessage("Expected the number of elements on page to be <%d> but was <%d>",
                        numberOfElements,
                        actual.getNumberOfElements()
                )
                .isEqualTo(numberOfElements);

        return this;
    }

    public PageAssert hasPageNumber(int pageNumber) {
        isNotNull();

        assertThat(actual.getNumber())
                .overridingErrorMessage("Expected page number to be <%d> but was <%d>",
                        pageNumber,
                        actual.getNumber()
                )
                .isEqualTo(pageNumber);

        return this;
    }

    public PageAssert hasPageSize(int pageSize) {
        isNotNull();

        assertThat(actual.getSize())
                .overridingErrorMessage("Expected page size to be <%d> but was <%d>",
                        pageSize,
                        actual.getSize()
                )
                .isEqualTo(pageSize);

        return this;
    }

    public PageAssert hasSort(Sort sort) {
        isNotNull();

        assertThat(actual.getSort())
                .overridingErrorMessage("Expected sort to be <%s> but was <%s>",
                        sort,
                        actual.getSort()
                )
                .isEqualTo(sort);

        return this;
    }

    public PageAssert hasTotalNumberOfElements(long totalNumberOfElements) {
        isNotNull();

        assertThat(actual.getTotalElements())
                .overridingErrorMessage("Expected total number of elements to be <%d> but was <%d>",
                        totalNumberOfElements,
                        actual.getTotalElements()
                )
                .isEqualTo(totalNumberOfElements);

        return this;
    }

    public PageAssert hasTotalNumberOfPages(int totalNumberOfPages) {
        isNotNull();

        assertThat(actual.getTotalPages())
                .overridingErrorMessage("Expected total number of pages to be <%d> but was <%d>",
                        totalNumberOfPages,
                        actual.getTotalPages()
                )
                .isEqualTo(totalNumberOfPages);

        return this;
    }

    public PageAssert isFirstPage() {
        isNotNull();

        assertThat(actual.isFirstPage())
                .overridingErrorMessage("Expected the page to be the first page but it was not")
                .isTrue();

        return this;
    }

    public PageAssert isLastPage() {
        isNotNull();

        assertThat(actual.isLastPage())
                .overridingErrorMessage("Expected the page to be the last page but it was not")
                .isTrue();

        assertThat(actual.hasNextPage())
                .overridingErrorMessage("Expected page to not have a next page but it has next page")
                .isFalse();

        return this;
    }

    public PageAssert isNotFirstPage() {
        isNotNull();

        assertThat(actual.isFirstPage())
                .overridingErrorMessage("Expected the page to not be the first page but it was")
                .isFalse();

        return this;
    }

    public PageAssert isNotLastPage() {
        isNotNull();

        assertThat(actual.isLastPage())
                .overridingErrorMessage("Expected the page to not be the last page but it was")
                .isFalse();

        assertThat(actual.hasNextPage())
                .overridingErrorMessage("Expected page to have a next page but it didn't have it")
                .isTrue();

        return this;
    }

    public PageAssert isEmpty() {
        isNotNull();

        assertThat(actual.getNumberOfElements())
                .overridingErrorMessage("Expected number of elements to be <0> but was <%d>",
                        actual.getNumberOfElements()
                )
                .isEqualTo(0);

        assertThat(actual.hasContent())
                .overridingErrorMessage("Expected page content to be empty but page had <%d> elements",
                        actual.getContent().size()
                )
                .isFalse();

        return this;
    }
}
