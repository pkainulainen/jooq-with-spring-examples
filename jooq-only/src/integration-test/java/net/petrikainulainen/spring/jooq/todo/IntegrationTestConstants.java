package net.petrikainulainen.spring.jooq.todo;

import net.petrikainulainen.spring.jooq.common.TestDateUtil;

/**
 * @author Petri Kainulainen
 */
public class IntegrationTestConstants {

    public static final String CURRENT_CREATION_TIME = "2012-10-21 11:13:28";
    public static final String CURRENT_DESCRIPTION = "Lorem ipsum";
    public static final String CURRENT_MODIFICATION_TIME = "2012-10-21 11:13:28";
    public static final String CURRENT_TITLE_FIRST_TODO = "FooRai";
    public static final String CURRENT_TITLE_SECOND_TODO = "Bar";

    public static final Long ID_FIRST_TODO = 1L;
    public static final Long ID_SECOND_TODO = 2L;

    public static final String NEW_CREATION_TIME = TestDateUtil.CURRENT_TIMESTAMP;
    public static final String NEW_DESCRIPTION = "description";
    public static final String NEW_MODIFICATION_TIME = TestDateUtil.CURRENT_TIMESTAMP;
    public static final String NEW_TITLE = "title";

    public static final String SEARCH_TERM = "iPSu";

    public static final String SORT_FIELD_NAME = "TITLE";


    private IntegrationTestConstants() {}
}
