package neueda.homework.util;

import neueda.homework.model.Category;
import neueda.homework.model.Entry;
import neueda.homework.model.Request;
import neueda.homework.model.Suite;
import org.junit.Test;

import static org.junit.Assert.*;

public class SuiteUtilsTest {
    @Test
    public void testValidateEntryNoA() throws Exception {
        final Entry entry = new Entry();
        entry.setName("sometoest");
        entry.setVariableB("1");
        entry.setResult("1");
        assertNotNull(entry);
        assertFalse(SuiteUtils.validateEntry(entry));
    }

    @Test
    public void testValidateEntryNoB() throws Exception {
        final Entry entry = new Entry();
        entry.setName("sometoest");
        entry.setVariableA("1");
        entry.setResult("1");
        assertNotNull(entry);
        assertFalse(SuiteUtils.validateEntry(entry));
    }

    @Test
    public void testValidateEntryNoResult() throws Exception {
        final Entry entry = new Entry();
        entry.setName("sometoest");
        entry.setVariableA("1");
        entry.setVariableB("1");
        assertNotNull(entry);
        assertFalse(SuiteUtils.validateEntry(entry));
    }

    @Test
    public void testValidateCategoryInvalidRequest() throws Exception {
        final Category category = new Category();
        final Request request = new Request();
        request.setPath("aaa");
        request.setMethod(null);
        final Entry entry = new Entry();
        entry.setName("sometoest");
        entry.setVariableA("1");
        entry.setVariableB("1");
        entry.setResult("1");
        category.addEntry(entry);
        assertNotNull(category);
        assertFalse(SuiteUtils.validateCategory(category));
    }

    @Test
    public void testValidateCategoryNoEntry() throws Exception {
        final Category category = new Category();
        final Request request = new Request();
        request.setPath("sometoest");
        category.setRequest(request);
        assertNotNull(category);
        assertFalse(SuiteUtils.validateCategory(category));
    }

    @Test
    public void testValidateCategoryInvalidEntry() throws Exception {
        final Category category = new Category();
        final Request request = new Request();
        request.setPath("sometoest");
        category.setRequest(request);

        final Entry entry = new Entry();
        entry.setName("sometoest");
        entry.setVariableA("1");
        category.addEntry(entry);

        assertNotNull(category);
        assertFalse(SuiteUtils.validateCategory(category));
    }

    @Test
    public void testValidateSuiteNoCategories() throws Exception {
        final Suite suite = new Suite();
        suite.setName("Test");
        assertNotNull(suite);
        assertFalse(SuiteUtils.validateSuite(suite));
    }

    @Test
    public void testValidateSuiteInvalidCategory() throws Exception {
        final Suite suite = new Suite();
        suite.setName("Test");
        suite.addCategory(new Category());
        assertNotNull(suite);
        assertFalse(SuiteUtils.validateSuite(suite));
    }

    @Test
    public void testValidateSuite() throws Exception {
        final Suite suite = new Suite();
        suite.setName("Test");
        final Category category = new Category();
        final Request request = new Request();
        request.setPath("sometoest");
        category.setRequest(request);

        final Entry entry = new Entry();
        entry.setName("sometoest");
        entry.setVariableA("1");
        entry.setVariableB("1");
        entry.setResult("1");
        category.addEntry(entry);
        suite.addCategory(category);
        assertNotNull(suite);
        assertTrue(SuiteUtils.validateSuite(suite));
    }

    @Test
    public void testValidateEntryInvalidEntry() throws Exception {
        assertFalse(SuiteUtils.validateEntry(null));
    }

    @Test
    public void testValidateNullCategory() throws Exception {
        assertFalse(SuiteUtils.validateCategory(null));
    }

    @Test
    public void testValidateNullSuite() throws Exception {
        assertFalse(SuiteUtils.validateSuite(null));
    }

    @Test
    public void testValidateNullRequest() throws Exception {
        assertFalse(SuiteUtils.validateRequestEntry(null));
    }
}