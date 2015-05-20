package neueda.homework.util;

import neueda.homework.model.Category;
import neueda.homework.model.Entry;
import neueda.homework.model.Request;
import neueda.homework.model.Suite;
import neueda.homework.model.xml.MindMap;
import neueda.homework.model.xml.MindMapNode;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kristaps Kohs
 */
public final class MindMapUtils {

    public static final String REQUEST_NODE_NAME = "request";
    public static final String PATH_KEY = "path";
    public static final String METHOD_KEY = "method";
    public static final String TEST_NAME_KEY = "test";
    public static final String VALUE_A_KEY = "a";
    public static final String VALUE_B_KEY = "b";
    public static final String RESULT_KEY = "result";

    private MindMapUtils() {
    }

    /**
     * Loads mind map from array of bytes.
     *
     * @param file loaded in memory file.
     * @return loaded map.
     * @throws Exception if error occurs while parsing map.
     */
    public static MindMap loadMinMap(final byte[] file) throws Exception {
        return loadMinMap(new ByteArrayInputStream(file));
    }

    /**
     * Loads mind map from input stream.
     *
     * @param stream mind map input stream.
     * @return loaded map.
     * @throws Exception if error occurs while parsing map.
     */
    public static MindMap loadMinMap(final InputStream stream) throws Exception {
        final JAXBContext context = JAXBContext.newInstance(MindMap.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        return (MindMap) unmarshaller.unmarshal(IOUtils.toBufferedInputStream(stream));
    }

    /**
     * Parses mindmap and assembles list of test suites from it.
     *
     * @param mindMap to parse.
     * @return list of parsed suites.
     */
    public static List<Suite> parseMindMap(final MindMap mindMap) {
        final List<Suite> suites = new ArrayList<>();
        for (MindMapNode node : mindMap.getNodes()) {
            final Suite suite = new Suite();
            suite.setName(node.getText());
            for (MindMapNode categoryNode : node.getNodes()) {
                suite.addCategory(parseCategory(categoryNode));
            }
            suites.add(suite);
        }
        return suites;
    }

    /**
     * Parses mind map node to assemble single category.
     *
     * @param categoryNode category node.
     * @return assembled category.
     */
    public static Category parseCategory(final MindMapNode categoryNode) {
        final Category category = new Category();
        category.setCategoryName(categoryNode.getText());
        for (MindMapNode entryNode : categoryNode.getNodes()) {
            if (StringUtils.equalsIgnoreCase(entryNode.getText(), REQUEST_NODE_NAME)) {
                category.setRequest(parseRequest(entryNode));
            } else {
                category.addEntry(parseEntry(entryNode));
            }
        }
        return category;
    }

    /**
     * Parses mindmap node to assemble request data.
     *
     * @param requestNode request node.
     * @return request.
     */
    public static Request parseRequest(final MindMapNode requestNode) {
        final Request request = new Request();
        final Map<String, String> values = new HashMap<>();
        for (MindMapNode mindMapNode : requestNode.getNodes()) {
            parseValue(mindMapNode.getText(), values);
        }

        for (String key : values.keySet()) {
            switch (key) {
                case PATH_KEY: {
                    request.setPath(values.get(key));
                }
                break;
                case METHOD_KEY: {
                    request.setMethod(EnumUtils.getEnum(Request.Method.class, values.get(key)));
                }
            }
        }
        return request;
    }

    /**
     * Parses mind map node to assemble single entry object..
     *
     * @param entryNode mind map node.
     * @return entry.
     */
    public static Entry parseEntry(final MindMapNode entryNode) {
        Entry entry = new Entry();
        final Map<String, String> values = new HashMap<>();
        parseValue(entryNode.getText(), values);
        for (MindMapNode mindMapNode : entryNode.getNodes()) {
            parseValue(mindMapNode.getText(), values);
        }

        for (String key : values.keySet()) {
            switch (key) {
                case TEST_NAME_KEY: {
                    entry.setName(values.get(key));
                }
                break;
                case VALUE_A_KEY: {
                    entry.setVariableA(values.get(key));
                }
                break;
                case VALUE_B_KEY: {
                    entry.setVariableB(values.get(key));
                }
                break;
                case RESULT_KEY: {
                    entry.setResult(values.get(key));
                }
                break;
            }
        }
        return entry;
    }

    /**
     * Parses key-value of node and adds to map of values if matches critteria.
     *
     * @param keyValueString key value string.
     * @param values         values.
     */
    public static void parseValue(final String keyValueString, final Map<String, String> values) {
        if (StringUtils.isNotBlank(keyValueString)) {
            final String[] keyValue = StringUtils.split(keyValueString, ":");
            if (ArrayUtils.getLength(keyValue) == 2) {
                values.put(StringUtils.lowerCase(StringUtils.trim(keyValue[0])), StringUtils.trim(keyValue[1]));
            }
        }
    }
}
