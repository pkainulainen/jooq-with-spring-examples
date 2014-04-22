package net.petrikainulainen.spring.jooq;

import org.springframework.http.MediaType;

import java.nio.charset.Charset;

/**
 * @author Petri Kainulainen
 */
public class WebTestConstants {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    public static final String ERROR_CODE_NOT_EMPTY = "NotEmpty";
    public static final String ERROR_CODE_LENGTH = "Length";

    public static final String REQUEST_PARAM_PAGE_NUMBER = "page";
    public static final String REQUEST_PARAM_PAGE_SIZE = "size";
    public static final String REQUEST_PARAM_SEARCH_TERM = "searchTerm";
    public static final String REQUEST_PARAM_SORT = "sort";

    private WebTestConstants() {
    }
}
