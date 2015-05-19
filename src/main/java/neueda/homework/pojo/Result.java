package neueda.homework.pojo;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author Kristaps Kohs
 */
public class Result {
    private final String processName;
    private final Object expected;
    private Object actual;
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

    public Object getExpected() {
        return expected;
    }

    public void setActual(Object actual) {
        this.actual = actual;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public boolean hasErrors() {
        return StringUtils.isNotBlank(error);
    }

    public boolean matches() {
        return Objects.equals(expected, actual);
    }
}
