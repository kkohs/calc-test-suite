package neueda.homework.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kristaps Kohs
 */
public class Suite {
    private String name;
    private List<Category> categories = new ArrayList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void addCategory(final Category category) {
        categories.add(category);
    }
}
