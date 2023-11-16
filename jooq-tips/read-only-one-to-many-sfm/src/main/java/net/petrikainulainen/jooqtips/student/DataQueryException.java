package net.petrikainulainen.jooqtips.student;

/**
 * This exception is thrown when the returned {@code ResultSet}
 * object cannot be transformed into the returned object.
 */
class DataQueryException extends RuntimeException {

    DataQueryException(String messageTemplate, Object... params) {
        super(String.format(messageTemplate, params));
    }

    DataQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
