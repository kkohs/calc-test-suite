package neueda.homework.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import neueda.homework.ex.InvalidResultException;
import neueda.homework.pojo.Entry;
import neueda.homework.pojo.Request;
import neueda.homework.pojo.Result;
import neueda.homework.pojo.Suite;
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

public class CalcDriverTest extends CalcTestBase {

    @Test
    public void testRunCompleteSuite() throws Exception {
        when(entity.getContent()).thenReturn(
                new ByteArrayInputStream(new ObjectMapper()
                        .writeValueAsBytes(Collections.singletonMap("result", "4"))));
        Suite suite = createValidSuite();
        final CalcDriver driver = new CalcDriverBuilder().setHttpClient(httpClient).build();
        List<Result> results = driver.runCompleteSuite(suite);
        for (Result result : results) {
            assertFalse(result.hasErrors());
            assertTrue(result.matches());
        }
    }

    @Test
    public void testRunSingleEntry() throws Exception {
        when(entity.getContent()).thenReturn(
                new ByteArrayInputStream(new ObjectMapper()
                        .writeValueAsBytes(Collections.singletonMap("result", "4"))));
        final CalcDriver driver = new CalcDriverBuilder().setHttpClient(httpClient).build();
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
        final CalcDriver driver = new CalcDriverBuilder().setHttpClient(httpClient).build();
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
        final CalcDriver driver = new CalcDriverBuilder().setHttpClient(httpClient).build();
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
        final CalcDriver driver = new CalcDriverBuilder().setHttpClient(httpClient).build();
        final Entry entry = createValidEntry();
        final Request request = createValidRequest();
        final Result result = driver.runSingleEntry(request, entry);
        assertTrue(result.hasErrors());
        assertFalse(result.matches());
    }

}