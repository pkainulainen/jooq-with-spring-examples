package net.petrikainulainen.spring.jooq.todo.exception;

/**
 * @author Petri Kainulainen
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
