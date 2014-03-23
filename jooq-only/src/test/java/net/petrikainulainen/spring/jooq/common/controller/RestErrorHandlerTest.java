package net.petrikainulainen.spring.jooq.common.controller;

import net.petrikainulainen.spring.jooq.common.dto.RestError;
import net.petrikainulainen.spring.jooq.common.dto.ValidationError;
import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petri Kainulainen
 */
public class RestErrorHandlerTest {

    private static final String FIELD_ERROR_CODE = "NotNull";
    private static final String FIELD_ERROR_DEFAULT_MESSAGE = "May not be null";
    private static final String FIELD_NAME = "field";
    private static final String OBJECT_NAME = "object";

    private RestErrorHandler errorHandler = new RestErrorHandler();

    @Test
    public void handleValidationError_WithOneFieldError_ShouldReturnRestErrorWithOneValidationError() throws NoSuchMethodException {

        MethodParameter invokedMethod = createInvokedMethod();

        FieldError fieldValidationError = createFieldError(FIELD_NAME, FIELD_ERROR_CODE, FIELD_ERROR_DEFAULT_MESSAGE);
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), OBJECT_NAME);
        bindingResult.addError(fieldValidationError);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(invokedMethod, bindingResult);

        RestError error = errorHandler.handleValidationError(ex);

        assertThat(error.getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(error.getMessage()).isNotEmpty();
        assertThat(error.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

        List<ValidationError> validationErrors = error.getValidationErrors();
        assertThat(validationErrors).hasSize(1);

        ValidationError validationError = validationErrors.get(0);
        assertThat(validationError.getField()).isEqualTo(FIELD_NAME);
        assertThat(validationError.getErrorCode()).isEqualTo(FIELD_ERROR_CODE);
        assertThat(validationError.getErrorMessage()).isEqualTo(FIELD_ERROR_DEFAULT_MESSAGE);
    }

    private FieldError createFieldError(String fieldName, String errorCode, String defaultMessage) {
        return new FieldError(OBJECT_NAME,
                fieldName,
                null,
                false,
                new String[]{errorCode},
                new Object[]{},
                defaultMessage
        );
    }

    private MethodParameter createInvokedMethod() throws NoSuchMethodException {
        return new MethodParameter(String.class.getMethod("indexOf", String.class), 1);
    }
}
