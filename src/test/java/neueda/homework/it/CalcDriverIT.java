package neueda.homework.it;

import neueda.homework.core.CalcDriver;
import neueda.homework.core.CalcDriverBuilder;
import neueda.homework.pojo.Result;
import neueda.homework.pojo.Suite;
import neueda.homework.pojo.xml.MindMap;
import neueda.homework.util.MindMapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ErrorCollector;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Kristaps Kohs
 */
public class CalcDriverIT {
    public static final String MIND_MAP_FILE_PROPERTY = "mind.map.file";
    public static final String DEFAULT_ENDPOINT = "calculator.neueda.lv";
    public static final String ENDPOINT_PROPERTY = "test.host";
    @Rule
    public ErrorCollector collector = new ErrorCollector();


    @Test
    public void testCompleteSuite() throws Exception {
        final String host = StringUtils.defaultString(System.getProperty(ENDPOINT_PROPERTY), DEFAULT_ENDPOINT);
        final List<Suite> suites = prepareTest();
        try (CalcDriver driver = new CalcDriverBuilder().setHost(host).build()) {
            for (Suite suite : suites) {
                if (MindMapUtils.validateSuite(suite)) {
                    final List<Result> results = driver.runCompleteSuite(suite);
                    for (Result result : results) {
                        collector.checkThat(result.getError(), result.hasErrors(), CoreMatchers.equalTo(false));
                        collector.checkThat("Results did not match for "
                                + result.getProcessName(), result.matches(), CoreMatchers.equalTo(true));
                    }
                } else {
                    collector.addError(new RuntimeException("Invalid suite provided" + suite.getName()));
                }
            }
        }
    }


    private List<Suite> prepareTest() throws Exception {
       final String mindMapFileName = System.getProperty(MIND_MAP_FILE_PROPERTY);
        byte[] mindMapData;
        if (StringUtils.isBlank(mindMapFileName)) {
            mindMapData = loadDefaultMindMap();
        } else {
            mindMapData = FileUtils.readFileToByteArray(FileUtils.getFile(mindMapFileName));
        }
        return MindMapUtils.parseMindMap(MindMapUtils.loadMinMap(mindMapData));
    }

    private byte[] loadDefaultMindMap() throws IOException {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("complete.mm")) {
            return IOUtils.toByteArray(stream);
        }
    }
}
