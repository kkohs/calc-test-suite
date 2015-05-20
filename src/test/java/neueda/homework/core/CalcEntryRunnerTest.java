package neueda.homework.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import neueda.homework.model.Entry;
import neueda.homework.model.Request;
import neueda.homework.model.Result;
import neueda.homework.model.Suite;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Kristaps Kohs
 */

public class CalcEntryRunnerTest extends CalcTestBase {

    @Test
    public void testRunCompleteSuite() throws Exception {
        when(entity.getContent()).thenReturn(
                new ByteArrayInputStream(new ObjectMapper()
                        .writeValueAsBytes(Collections.singletonMap("result", "4"))));
        Suite suite = createValidSuite();
        final CalcEntryRunner driver = new CalcRunnerBuilder().setHttpClient(httpClient).build();
        List<Result> results = driver.runCompleteSuite(suite);
        for (Result result : results) {
            assertFalse(result.hasErrors());
            assertTrue(result.matches());
        }
    }    
    @Test
    public void testRunCompleteSuiteInvalidResult() throws Exception {
        when(entity.getContent()).thenReturn(
                new ByteArrayInputStream(new ObjectMapper()
                        .writeValueAsBytes(Collections.singletonMap("result", "10"))));
        Suite suite = createValidSuite();
        final CalcEntryRunner driver = new CalcRunnerBuilder().setHttpClient(httpClient).build();
        List<Result> results = driver.runCompleteSuite(suite);
        for (Result result : results) {
            assertFalse(result.hasErrors());
            assertFalse(result.matches());
        }
    }    
    
    @Test
    public void testRunCompleteSuiteErrorInResult() throws Exception {
        when(entity.getContent()).thenReturn(
                new ByteArrayInputStream(new ObjectMapper()
                        .writeValueAsBytes(Collections.singletonMap("result", "4"))));
        when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_BAD_REQUEST);
        Suite suite = createValidSuite();
        final CalcEntryRunner driver = new CalcRunnerBuilder().setHttpClient(httpClient).build();
        List<Result> results = driver.runCompleteSuite(suite);
        for (Result result : results) {
            assertTrue(result.hasErrors());
            assertFalse(result.matches());
        }
    }

    @Test
    public void testRunSingleEntry() throws Exception {
        when(entity.getContent()).thenReturn(
                new ByteArrayInputStream(new ObjectMapper()
                        .writeValueAsBytes(Collections.singletonMap("result", "4"))));
        final CalcEntryRunner driver = new CalcRunnerBuilder().setHttpClient(httpClient).build();
        final Entry entry = createValidEntry();
        final Request request = createValidRequest();
        final Result result = driver.runSingleEntry(request, entry);
        assertFalse(result.hasErrors());
        assertTrue(result.matches());
    }

    @Test
    public void testRunSingleEntryUnmachingResult() throws Exception {
        when(entity.getContent()).thenReturn(
                new ByteArrayInputStream(new ObjectMapper()
                        .writeValueAsBytes(Collections.singletonMap("result", "8"))));
        final CalcEntryRunner driver = new CalcRunnerBuilder().setHttpClient(httpClient).build();
        final Entry entry = createValidEntry();
        final Request request = createValidRequest();
        final Result result = driver.runSingleEntry(request, entry);
        assertFalse(result.hasErrors());
        assertFalse(result.matches());
    }

    @Test
    public void testRunSingleEntryInvalidHttpResponse() throws Exception {
        when(entity.getContent()).thenReturn(
                new ByteArrayInputStream(new ObjectMapper()
                        .writeValueAsBytes(Collections.singletonMap("result", "4"))));
        when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_BAD_REQUEST);
        final CalcEntryRunner driver = new CalcRunnerBuilder().setHttpClient(httpClient).build();
        final Entry entry = createValidEntry();
        final Request request = createValidRequest();
        final Result result = driver.runSingleEntry(request, entry);
        assertTrue(result.hasErrors());
        assertFalse(result.matches());
    }

    @Test
    public void testRunSingleEntryInvalidResponse() throws Exception {
        when(entity.getContent()).thenReturn(
                new ByteArrayInputStream("NOT A JSON ----OBJECT".getBytes()));
        final CalcEntryRunner driver = new CalcRunnerBuilder().setHttpClient(httpClient).build();
        final Entry entry = createValidEntry();
        final Request request = createValidRequest();
        final Result result = driver.runSingleEntry(request, entry);
        assertTrue(result.hasErrors());
        assertFalse(result.matches());
    }

}