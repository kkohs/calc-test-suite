package neueda.homework.util;

import neueda.homework.pojo.Category;
import neueda.homework.pojo.Entry;
import neueda.homework.pojo.Request;
import neueda.homework.pojo.Suite;
import neueda.homework.pojo.xml.MindMap;
import neueda.homework.pojo.xml.MindMapNode;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.rules.ErrorCollector;

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
public class MindMapUtils {

    public static final String REQUEST_NODE_NAME = "request";

    public static MindMap loadMinMap(final byte[] file) throws Exception {
        return loadMinMap(new ByteArrayInputStream(file));
    }

    public static MindMap loadMinMap(final InputStream stream) throws Exception {
        final JAXBContext context = JAXBContext.newInstance(MindMap.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (MindMap) unmarshaller.unmarshal(IOUtils.toBufferedInputStream(stream));
    }


    public static List<Suite> parseMindMap(final MindMap mindMap) {
        final List<Suite> suites = new ArrayList<>();
        for (MindMapNode node : mindMap.getNodes()) {
            Suite suite = new Suite();
            suite.setName(node.getText());
            for (MindMapNode categoryNode : node.getNodes()) {
                Category category = parseCategory(categoryNode);
                if (validateCategory(category)) {
                    suite.addCategory(parseCategory(categoryNode));
                }
            }
            suites.add(suite);
        }
        return suites;
    }


    public static Category parseCategory(final MindMapNode categoryNode) {
        Category category = new Category();
        category.setName(categoryNode.getText());
        for (MindMapNode entryNode : categoryNode.getNodes()) {
            if (StringUtils.equalsIgnoreCase(entryNode.getText(), REQUEST_NODE_NAME)) {
                Request request = parseRequest(entryNode);
                if (validateRequestEntry(request)) {
                    category.setRequest(request);
                }
            } else {
                final Entry entry = parseEntry(entryNode);
                if (validateEntry(entry)) {
                    category.addEntry(entry);
                }
            }
        }
        return category;
    }

    public static Request parseRequest(final MindMapNode requestNode) {
        Request request = new Request();
        final Map<String, String> values = new HashMap<>();
        for (MindMapNode mindMapNode : requestNode.getNodes()) {
            parseValue(mindMapNode.getText(), values);
        }

        for (String key : values.keySet()) {
            switch (key) {
                case "path": {
                    request.setPath(values.get(key));
                }
                break;
                case "method": {
                    request.setMethod(EnumUtils.getEnum(Request.Method.class, values.get(key)));
                }
            }
        }
        return request;
    }

    public static Entry parseEntry(final MindMapNode entryNode) {
        Entry entry = new Entry();
        final Map<String, String> values = new HashMap<>();
        parseValue(entryNode.getText(), values);
        for (MindMapNode mindMapNode : entryNode.getNodes()) {
            parseValue(mindMapNode.getText(), values);
        }

        for (String key : values.keySet()) {
            switch (key) {
                case "test": {
                    entry.setName(values.get(key));
                }
                break;
                case "a": {
                    entry.setVariableA(values.get(key));
                }
                break;
                case "b": {
                    entry.setVariableB(values.get(key));
                }
                break;
                case "result": {
                    entry.setResult(values.get(key));
                }
                break;
            }
        }
        return entry;
    }

    public static void parseValue(final String keyValueString, final Map<String, String> values) {
        if (StringUtils.isNotBlank(keyValueString)) {
            final String[] keyValue = StringUtils.split(keyValueString, ":");
            if (ArrayUtils.getLength(keyValue) == 2) {
                values.put(StringUtils.lowerCase(StringUtils.trim(keyValue[0])), StringUtils.trim(keyValue[1]));
            }
        }
    }

    public static boolean validateRequestEntry(final Request request) {
        return request != null && request.getMethod() != null && StringUtils.isNotBlank(request.getPath());
    }

    public static boolean validateEntry(final Entry entry) {
        return entry != null && StringUtils.isNotBlank(entry.getResult())
                && StringUtils.isNotBlank(entry.getVariableA()) && StringUtils.isNotBlank(entry.getVariableB());
    }

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
        }       new ErrorCollector();
        return true;
    }

}
