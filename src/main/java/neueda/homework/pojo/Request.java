package neueda.homework.pojo;

/**
 * Class representing request data for a {@link neueda.homework.pojo.Category}.
 *
 * @author Kristaps Kohs
 */
public class Request {
    /** Path of the request. */
    private String path;
    /** Method used in request. */
    private Method method = Method.GET;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    /** HTTP method types. */
    public static enum Method {
        GET
    }
}
