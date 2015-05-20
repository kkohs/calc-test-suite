package neueda.homework.it;

import neueda.homework.core.CalcEntryRunner;
import neueda.homework.core.CalcRunnerBuilder;
import neueda.homework.pojo.Entry;
import neueda.homework.pojo.Request;
import neueda.homework.pojo.Result;
import neueda.homework.pojo.Suite;
import neueda.homework.util.MindMapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
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
        try (CalcEntryRunner driver = new CalcRunnerBuilder().setHost(host).build()) {
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
    
    @Test
    public void testSingleEntry() throws Exception {
        final String host = StringUtils.defaultString(System.getProperty(ENDPOINT_PROPERTY), DEFAULT_ENDPOINT);
        final Request request = new Request();
        request.setPath("/api/multiply");
        
        final Entry entry = new Entry();
        entry.setName("Single entry multiplication tests");
        entry.setVariableA("958456851365");
        entry.setVariableB("99999999");
        entry.setResult("95845684178043148635");
        try (CalcEntryRunner driver = new CalcRunnerBuilder().setHost(host).build()) {
            final Result result = driver.runSingleEntry(request, entry);
            Assert.assertFalse(result.hasErrors());
            Assert.assertTrue(result.matches());
        }
    }  
    
    @Test
    public void testSingleEntryInvalidResult() throws Exception {
        final String host = StringUtils.defaultString(System.getProperty(ENDPOINT_PROPERTY), DEFAULT_ENDPOINT);
        final Request request = new Request();
        request.setPath("/api/multiply");
        
        final Entry entry = new Entry();
        entry.setName("Single entry multiplication tests");
        entry.setVariableA("958456851365");
        entry.setVariableB("99999999");
        entry.setResult("5");
        try (CalcEntryRunner driver = new CalcRunnerBuilder().setHost(host).build()) {
            final Result result = driver.runSingleEntry(request, entry);
            Assert.assertFalse(result.hasErrors());
            Assert.assertFalse(result.matches());
        }
    }  
    
    @Test
    public void testSingleEntryWithErrors() throws Exception {
        final String host = StringUtils.defaultString(System.getProperty(ENDPOINT_PROPERTY), DEFAULT_ENDPOINT);
        final Request request = new Request();
        request.setPath("/api/multiply");
        
        final Entry entry = new Entry();
        entry.setName("Single entry multiplication tests");
        entry.setVariableA("958456851365");
        entry.setVariableB("NOT A NUMBER");
        entry.setResult("5");
        try (CalcEntryRunner driver = new CalcRunnerBuilder().setHost(host).build()) {
            final Result result = driver.runSingleEntry(request, entry);
            Assert.assertTrue(result.hasErrors());
            Assert.assertFalse(result.matches());
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
