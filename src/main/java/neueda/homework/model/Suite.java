package neueda.homework.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing signle test suite with multiple {@link neueda.homework.model.Category} to execute.
 *
 * @author Kristaps Kohs
 */
public class Suite {
    /** Sutie name. */
    private String name;
    /** List of categories in suite. */
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


    public void addCategory(final Category category) {
        categories.add(category);
    }
}
