package net.petrikainulainen.spring.jooq.common.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Petri Kainulainen
 */
public class ValidationError {

    private final String errorCode;
    private final String errorMessage;
    private final String field;

    public ValidationError(String field, String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.field = field;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getField() {
        return field;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("field", field)
                .append("errorCode", errorCode)
                .append("errorMessage", errorMessage)
                .build();
    }
}
