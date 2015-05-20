package neueda.homework.util;

import neueda.homework.pojo.Category;
import neueda.homework.pojo.Entry;
import neueda.homework.pojo.Request;
import neueda.homework.pojo.Suite;
import neueda.homework.pojo.xml.MindMap;
import neueda.homework.pojo.xml.MindMapNode;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MindMapUtilsTest {
    @Test
    public void testLoadMinMap() throws Exception {
        final InputStream stream = getClass().getClassLoader().getResourceAsStream("complete.mm");
        final MindMap mindMap = MindMapUtils.loadMinMap(stream);
        assertNotNull(mindMap);
        IOUtils.closeQuietly(stream);
    }

    @Test
    public void testLoadMinMap1() throws Exception {
        final InputStream stream = getClass().getClassLoader().getResourceAsStream("complete.mm");
        byte[] file = IOUtils.toByteArray(stream);
        IOUtils.closeQuietly(stream);
        MindMap mindMap =
                MindMapUtils.loadMinMap(file);
        assertNotNull(mindMap);
    }

    @Test(expected = JAXBException.class)
    public void testEmptyFile() throws Exception {
        MindMapUtils.loadMinMap(new byte[0]);
    }

    @Test(expected = JAXBException.class)
    public void testEmptyStream() throws Exception {
        MindMapUtils.loadMinMap(new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testParseMindMap() throws Exception {
        final InputStream stream = getClass().getClassLoader().getResourceAsStream("complete.mm");
        final MindMap mindMap = MindMapUtils.loadMinMap(stream);
        IOUtils.closeQuietly(stream);
        assertNotNull(mindMap);

        final List<Suite> suites = MindMapUtils.parseMindMap(mindMap);
        assertFalse(suites.isEmpty());
        for (Suite suite : suites) {
            assertEquals(suite.getName(), "Calculator tests");
            assertFalse(suite.getCategories().isEmpty());
        }
    }


    @Test
    public void testParseCategoryNode() throws Exception {
        final MindMapNode categoryNode = new MindMapNode();
        categoryNode.setText("Multiply");
        categoryNode.getNodes().add(createRequestNode("GET", "/path/"));
        categoryNode.getNodes().add(createEntryNode("sometest", "1", "2", "3"));

        final Category category = MindMapUtils.parseCategory(categoryNode);
        assertNotNull(category);
        assertTrue(MindMapUtils.validateCategory(category));
        assertEquals("Multiply", category.getCategoryName());
        assertNotNull(category.getRequest());
        assertFalse(category.getEntries().isEmpty());

    }

    @Test
    public void testParseRequestNode() throws Exception {
        final MindMapNode requestNode = createRequestNode("GET", "/path");
        final Request request = MindMapUtils.parseRequest(requestNode);
        assertNotNull(request);
        assertTrue(MindMapUtils.validateRequestEntry(request));
        assertEquals(Request.Method.GET, request.getMethod());
        assertEquals("/path", request.getPath());
    }

    @Test
    public void testParseInvalidRequest() throws Exception {
        final MindMapNode requestNode = createRequestNode("POST", "/path");
        final Request request = MindMapUtils.parseRequest(requestNode);
        assertNotNull(request);
        assertFalse(MindMapUtils.validateRequestEntry(request));
    }

    @Test
    public void testParseEntryNode() throws Exception {
        final MindMapNode entryNode = createEntryNode("sometest", "1", "2", "3");
        final Entry entry = MindMapUtils.parseEntry(entryNode);
        assertNotNull(entry);
        assertTrue(MindMapUtils.validateEntry(entry));
        assertEquals(entry.getName(), "sometest");
        assertEquals(entry.getVariableA(), "1");
        assertEquals(entry.getVariableB(), "2");
        assertEquals(entry.getResult(), "3");
    }

    @Test
    public void testParseInvalidEntryNode() throws Exception {
        final MindMapNode entryNode = createEntryNode("sometest", "1", "2", "3");
        MindMapNode testDataNode = new MindMapNode();
        testDataNode.setText("notvalid: ");
        entryNode.getNodes().add(testDataNode);

        final Entry entry = MindMapUtils.parseEntry(entryNode);
        assertNotNull(entry);
        assertTrue(MindMapUtils.validateEntry(entry));
        assertEquals(entry.getName(), "sometest");
        assertEquals(entry.getVariableA(), "1");
        assertEquals(entry.getVariableB(), "2");
        assertEquals(entry.getResult(), "3");
    }

    @Test
    public void testValidateEntryNoA() throws Exception {
        final Entry entry = new Entry();
        entry.setName("sometoest");
        entry.setVariableB("1");
        entry.setResult("1");
        assertNotNull(entry);
        assertFalse(MindMapUtils.validateEntry(entry));
    }

    @Test
    public void testValidateEntryNoB() throws Exception {
        final Entry entry = new Entry();
        entry.setName("sometoest");
        entry.setVariableA("1");
        entry.setResult("1");
        assertNotNull(entry);
        assertFalse(MindMapUtils.validateEntry(entry));
    }

    @Test
    public void testValidateEntryNoResult() throws Exception {
        final Entry entry = new Entry();
        entry.setName("sometoest");
        entry.setVariableA("1");
        entry.setVariableB("1");
        assertNotNull(entry);
        assertFalse(MindMapUtils.validateEntry(entry));
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
        assertFalse(MindMapUtils.validateCategory(category));
    }

    @Test
    public void testValidateCategoryNoEntry() throws Exception {
        final Category category = new Category();
        final Request request = new Request();
        request.setPath("sometoest");
        category.setRequest(request);
        assertNotNull(category);
        assertFalse(MindMapUtils.validateCategory(category));
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
        assertFalse(MindMapUtils.validateCategory(category));
    }

    @Test
    public void testValidateSuiteNoCategories() throws Exception {
        final Suite suite = new Suite();
        suite.setName("Test");
        assertNotNull(suite);
        assertFalse(MindMapUtils.validateSuite(suite));
    }

    @Test
    public void testValidateSuiteInvalidCategory() throws Exception {
        final Suite suite = new Suite();
        suite.setName("Test");
        suite.addCategory(new Category());
        assertNotNull(suite);
        assertFalse(MindMapUtils.validateSuite(suite));
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
        assertTrue(MindMapUtils.validateSuite(suite));
    }

    @Test
    public void testValidateEntryInvalidEntry() throws Exception {
        assertFalse(MindMapUtils.validateEntry(null));
    }

    @Test
    public void testValidateNullCategory() throws Exception {
        assertFalse(MindMapUtils.validateCategory(null));
    }

    @Test
    public void testValidateNullSuite() throws Exception {
        assertFalse(MindMapUtils.validateSuite(null));
    }

    @Test
    public void testValidateNullRequest() throws Exception {
        assertFalse(MindMapUtils.validateRequestEntry(null));
    }

    @Test
    public void testParseMissingDataEntryNode() throws Exception {
        final MindMapNode testNode = new MindMapNode();
        testNode.setText("Test: test name");
        testNode.getNodes().add(createNode("a:", "1"));
        testNode.getNodes().add(createNode("notvalid:", "1"));
        testNode.getNodes().add(createNode("result:", "1"));


        final Entry entry = MindMapUtils.parseEntry(testNode);
        assertFalse(MindMapUtils.validateEntry(entry));
        assertNotNull(entry);
        assertEquals(entry.getName(), "test name");
        assertEquals(entry.getVariableA(), "1");
        assertNull(entry.getVariableB());
        assertEquals(entry.getResult(), "1");
    }

    @Test
    public void testParseValue() throws Exception {
        Map<String, String> values = new HashMap<>();
        MindMapUtils.parseValue("key:value", values);
        assertTrue(values.containsKey("key"));
        assertEquals(values.get("key"), "value");
    }

    @Test
    public void parseInvalidValue() throws Exception {
        Map<String, String> values = new HashMap<>();
        MindMapUtils.parseValue("keyvalue", values);
        assertTrue(values.isEmpty());
    }    
    @Test
    public void parseInvalidValue3() throws Exception {
        Map<String, String> values = new HashMap<>();
        MindMapUtils.parseValue("", values);
        assertTrue(values.isEmpty());
    }

    @Test
    public void parseInvalidValue2() throws Exception {
        Map<String, String> values = new HashMap<>();
        MindMapUtils.parseValue("key:va:lue", values);
        assertTrue(values.isEmpty());
    }

    private static MindMapNode createRequestNode(final String method, final String path) {
        final MindMapNode requestNode = new MindMapNode();
        requestNode.setText("Request");
        requestNode.getNodes().add(createNode("Method", method));
        requestNode.getNodes().add(createNode("Path", path));
        return requestNode;
    }

    private static MindMapNode createEntryNode(final String name, final String valueA, final String valueB,
                                               final String result) {
        final MindMapNode testNode = new MindMapNode();
        testNode.setText("Test: " + name);
        testNode.getNodes().add(createNode("a", valueA));
        testNode.getNodes().add(createNode("b", valueB));
        testNode.getNodes().add(createNode("result", result));
        return testNode;
    }

    private static MindMapNode createNode(final String key, final String valueA) {
        MindMapNode testDataNode = new MindMapNode();
        testDataNode.setText(key + ":" + valueA);
        return testDataNode;
    }
}