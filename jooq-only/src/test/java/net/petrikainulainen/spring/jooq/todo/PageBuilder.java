package net.petrikainulainen.spring.jooq.todo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Petri Kainulainen
 */
public class PageBuilder<T> {

    private T[] itemsOnPage;

    private int pageNumber;

    private int pageSize;

    private Sort sort;

    private long totalNumberOfItems = 0L;

    public PageBuilder() {

    }

    public PageBuilder<T> itemsOnPage(T... itemsOnPage) {
        this.itemsOnPage = itemsOnPage;
        return this;
    }

    public PageBuilder<T> pageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public PageBuilder<T> pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public PageBuilder<T> sort(Sort sort) {
        this.sort = sort;
        return this;
    }

    public PageBuilder totalNumberOfItems(long totalNumberOfItems) {
        this.totalNumberOfItems = totalNumberOfItems;
        return this;
    }

    public Page<T> build() {
        Pageable pageRequest = new PageRequest(pageNumber, pageSize, sort);
        List<T> itemsOnPage = getItemsOnPage();
        return new PageImpl<>(itemsOnPage, pageRequest, totalNumberOfItems);
    }

    private List<T> getItemsOnPage() {
        if (this.itemsOnPage == null) {
            return new ArrayList<>();
        }
        else {
            return Arrays.asList(itemsOnPage);
        }
    }
}
