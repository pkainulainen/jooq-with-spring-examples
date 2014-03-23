package net.petrikainulainen.spring.jooq.common.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Petri Kainulainen
 */
public class RestError {

    private final int code;
    private final String message;
    private final HttpStatus status;

    private List<ValidationError> validationErrors;

    private RestError(Builder builder) {
        this.code = builder.code;
        this.message = builder.message;
        this.status = builder.status;
        this.validationErrors = builder.validationErrors;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("status", status)
                .append("code", code)
                .append("message", message)
                .append("validationErrors", validationErrors)
                .build();
    }

    public static class Builder {
        private int code;
        private String message;
        private HttpStatus status;
        private List<ValidationError> validationErrors = new ArrayList<>();

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder validationError(String field, String errorCode, String errorMessage) {
            ValidationError error = new ValidationError(field, errorCode, errorMessage);
            validationErrors.add(error);
            return this;
        }

        public RestError build() {
            return new RestError(this);
        }
    }
}
