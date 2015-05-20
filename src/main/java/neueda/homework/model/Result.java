package neueda.homework.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * Class representing single entry execution result.
 *
 * @author Kristaps Kohs
 */
public class Result {
    /** NAme of the process. */
    private final String processName;
    /** Expected result. */
    private final Object expected;
    /** Actual result. */
    private Object actual;
    /** Error of the process. */
    private String error;

    public Result(String processName, Object expected) {
        this.processName = processName;
        this.expected = expected;
    }

    public String getProcessName() {
        return processName;
    }

    public Object getActual() {
        return actual;
    }

    public void setActual(Object actual) {
        this.actual = actual;
    }

    public Object getExpected() {
        return expected;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    /**
     * Checks if result contains any errors.
     *
     * @return true if errors are present.
     */
    public boolean hasErrors() {
        return StringUtils.isNotBlank(error);
    }

    /**
     * Checks if expected result matches actual.
     *
     * @return true if matches.
     */
    public boolean matches() {
        return Objects.equals(expected, actual);
    }
}
