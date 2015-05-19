package neueda.homework.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import neueda.homework.ex.InvalidResultException;
import neueda.homework.pojo.Entry;
import neueda.homework.pojo.Request;
import neueda.homework.pojo.Result;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @author Kristaps Kohs
 */

public class CalcDriverTest extends CalcTestBase {

    @Test
    public void testRunCompleteSuite() throws Exception {


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
        assertTrue(result.matches());
    }

    @Test(expected = InvalidResultException.class)
    public void testRunSingleEntryUnmachingResult() throws Exception {
        when(entity.getContent()).thenReturn(
                new ByteArrayInputStream(new ObjectMapper()
                        .writeValueAsBytes(Collections.singletonMap("result", "8"))));
        final CalcDriver driver = new CalcDriverBuilder().setHttpClient(httpClient).build();
        final Entry entry = createValidEntry();
        final Request request = createValidRequest();
       final Result result = driver.runSingleEntry(request, entry);
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
    }

    private Request createValidRequest() {
        Request request = new Request();
        request.setMethod(Request.Method.GET);
        request.setPath("/mock/");
        return request;
    }

    private Entry createValidEntry() {
        Entry entry = new Entry();
        entry.setResult("4");
        entry.setVariableA("2");
        entry.setVariableB("2");
        entry.setName("Mock");
        return entry;
    }
}