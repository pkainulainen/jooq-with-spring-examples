package net.petrikainulainen.spring.jooq.common.controller;

import net.petrikainulainen.spring.jooq.common.dto.RestError;
import net.petrikainulainen.spring.jooq.todo.exception.TodoNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * @author Petri Kainulainen
 */
@ControllerAdvice
public class RestErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestErrorHandler.class);

    @ExceptionHandler(TodoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleTodoNotFound(TodoNotFoundException ex) {
        LOGGER.info("Todo entry was not found. Returning HTTP status code 404");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestError handleValidationError(MethodArgumentNotValidException ex) {
        LOGGER.info("Handling validation error");

        RestError.Builder error = RestError.getBuilder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage());

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        for (FieldError fieldError: fieldErrors) {
            error.validationError(fieldError.getField(), fieldError.getCode(), fieldError.getDefaultMessage());
        }

        RestError validationError = error.build();

        LOGGER.info("Returning validation error: {}", validationError);

        return validationError;
    }
}
