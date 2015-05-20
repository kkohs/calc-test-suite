package neueda.homework.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Class representing single category of test. i.e "Division"
 * @author Kristaps Kohs
 */
public class Category {
    /**Name of category.*/
    private String categoryName;
    /**Request for category.*/
    private Request request;
    /**List of test entries in this category.*/
    private List<Entry> entries = new ArrayList<>();

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
