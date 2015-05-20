package neueda.homework.util;

import neueda.homework.model.Category;
import neueda.homework.model.Entry;
import neueda.homework.model.Request;
import neueda.homework.model.Suite;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Kristaps Kohs
 */
public final class SuiteUtils {
    private SuiteUtils() {

    }

    /**
     * Validates request object.
     *
     * @param request to validate.
     * @return true if valid.
     */
    public static boolean validateRequestEntry(final Request request) {
        return request != null && request.getMethod() != null && StringUtils.isNotBlank(request.getPath());
    }

    /**
     * Validates entry object.
     *
     * @param entry to validate.
     * @return true if valid.
     */
    public static boolean validateEntry(final Entry entry) {
        return entry != null && StringUtils.isNotBlank(entry.getResult())
                && StringUtils.isNotBlank(entry.getVariableA()) && StringUtils.isNotBlank(entry.getVariableB());
    }

    /**
     * Validates category object.
     *
     * @param category to validate.
     * @return true if valid.
     */
    public static boolean validateCategory(final Category category) {
        if (category == null) {
            return false;
        } else if (!validateRequestEntry(category.getRequest())) {
            return false;
        } else if (category.getEntries().isEmpty()) {
            return false;
        }
        for (Entry entry : category.getEntries()) {
            if (!validateEntry(entry)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates suite object.
     *
     * @param suite to validate.
     * @return true if valid.
     */
    public static boolean validateSuite(final Suite suite) {
        if (suite == null) {
            return false;
        } else if (suite.getCategories().isEmpty()) {
            return false;
        }

        for (Category category : suite.getCategories()) {
            if (!validateCategory(category)) {
                return false;
            }
        }
        return true;
    }
}
