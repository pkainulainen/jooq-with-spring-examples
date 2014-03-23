package net.petrikainulainen.spring.jooq.common.dto;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petri Kainulainen
 */
public class RestErrorTest {

    private static final String ERROR_MESSAGE = "Internal server error";

    private static final String VALIDATION_ERROR_CODE = "NotNull";
    private static final String VALIDATION_ERROR_FIELD = "field";
    private static final String VALIDATION_ERROR_MESSAGE = "message";

    @Test
    public void build_NoValidationErrors_ShouldCreateRestErrorWithoutValidationErrors() {
        RestError error = RestError.getBuilder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ERROR_MESSAGE)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();

        assertThat(error.getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(error.getMessage()).isEqualTo(ERROR_MESSAGE);
        assertThat(error.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(error.getValidationErrors()).isEmpty();
    }

    @Test
    public void build_WithOneValidationError_ShouldCreateRestErrorWithOneValidationError() {
        RestError error = RestError.getBuilder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ERROR_MESSAGE)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .validationError(VALIDATION_ERROR_FIELD, VALIDATION_ERROR_CODE, VALIDATION_ERROR_MESSAGE)
                .build();

        assertThat(error.getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(error.getMessage()).isEqualTo(ERROR_MESSAGE);
        assertThat(error.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(error.getValidationErrors()).hasSize(1);

        ValidationError validationError = error.getValidationErrors().get(0);

        assertThat(validationError.getErrorCode()).isEqualTo(VALIDATION_ERROR_CODE);
        assertThat(validationError.getErrorMessage()).isEqualTo(VALIDATION_ERROR_MESSAGE);
        assertThat(validationError.getField()).isEqualTo(VALIDATION_ERROR_FIELD);
    }
}
