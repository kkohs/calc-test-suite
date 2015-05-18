package neueda.homework.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kristaps Kohs
 */
public class Category {
    private String name;
    private Request request;
    private List<Entry> entries = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public void addEntry(final Entry entry) {
        entries.add(entry);
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
