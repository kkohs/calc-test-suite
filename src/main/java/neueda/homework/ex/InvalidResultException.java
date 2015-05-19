package neueda.homework.ex;

/**
 * @author Kristaps Kohs
 */
public class InvalidResultException extends RuntimeException {
    private final Object expected;
    private final Object actual;

    public InvalidResultException(Object expected, Object actual) {
        this.expected = expected;
        this.actual = actual;
    }

    public InvalidResultException(String message, Object expected, Object actual) {
        super(message);
        this.expected = expected;
        this.actual = actual;
    }

    public InvalidResultException(String message, Throwable cause, Object expected, Object actual) {
        super(message, cause);
        this.expected = expected;
        this.actual = actual;
    }

    public Object getExpected() {
        return expected;
    }

    public Object getActual() {
        return actual;
    }
}
