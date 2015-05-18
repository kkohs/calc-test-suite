package neueda.homework.util;

import neueda.homework.pojo.Category;
import neueda.homework.pojo.Entry;
import neueda.homework.pojo.Request;
import neueda.homework.pojo.Suite;
import neueda.homework.pojo.xml.MindMap;
import neueda.homework.pojo.xml.MindMapNode;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kristaps Kohs
 */
public class MindMapUtils {

    public static final String REQUEST_NODE_NAME = "request";

    public static List<Suite> parseMindMap(final MindMap mindMap) {
        final List<Suite> suites = new ArrayList<>();
        for (MindMapNode node : mindMap.getNodes()) {
            Suite suite = new Suite();
            suite.setName(node.getText());
            for (MindMapNode categoryNode : node.getNodes()) {
                suite.addCategory(parseCategory(categoryNode));
            }
            suites.add(suite);
        }
        return suites;
    }


    private static Category parseCategory(final MindMapNode categoryNode) {
        Category category = new Category();
        category.setName(categoryNode.getText());
        for (MindMapNode entryNode : categoryNode.getNodes()) {
            if (StringUtils.equalsIgnoreCase(entryNode.getText(), REQUEST_NODE_NAME)) {
                category.setRequest(parseRequest(entryNode));
            } else {
                category.addEntry(parseEntry(entryNode));
            }
        }
        return category;
    }

    private static Request parseRequest(final MindMapNode requestNode) {
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

    private static Entry parseEntry(final MindMapNode entryNode) {
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

    private static void parseValue(final String keyValueString, final Map<String, String> values) {
        final String[] keyValue = StringUtils.split(keyValueString, ":");
        values.put(StringUtils.lowerCase(StringUtils.trim(keyValue[0])), StringUtils.trim(keyValue[1]));
    }

}
