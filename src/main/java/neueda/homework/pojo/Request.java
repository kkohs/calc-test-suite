package neueda.homework.pojo;

/**
 * @author Kristaps Kohs
 */
public class Request {
    public static enum Method {POST, GET}
    private String path;
    private Method method;

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
}
