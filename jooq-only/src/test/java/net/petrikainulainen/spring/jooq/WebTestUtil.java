package net.petrikainulainen.spring.jooq;

/**
 * @author Petri Kainulainen
 */
public class WebTestUtil {

    public static String createStringWithLength(int length) {
        StringBuilder string = new StringBuilder();

        for (int index = 0; index < length; index++) {
            string.append("A");
        }

        return string.toString();
    }

    public static String createSortParameterValue(String sortField, String sortOrder) {
        return sortField + "," + sortOrder;
    }
}
