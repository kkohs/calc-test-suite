package neueda.homework.core;

import neueda.homework.pojo.Category;
import neueda.homework.pojo.Entry;
import neueda.homework.pojo.Request;
import neueda.homework.pojo.Suite;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

/**
 * @author Kristaps Kohs
 */
public abstract class CalcTestBase {
    @Mock
    protected HttpClient httpClient;
    @Mock
    protected HttpResponse response;
    @Mock
    protected StatusLine statusLine;
    @Mock
    protected HttpEntity entity;
    @Before
    public void initMocks() throws Exception{
        MockitoAnnotations.initMocks(this);
        stub(response.getStatusLine()).toReturn(statusLine);
        stub(response.getEntity()).toReturn(entity);
        stub(statusLine.getStatusCode()).toReturn(HttpStatus.SC_OK);
        stub(httpClient.execute(any(HttpHost.class), any(HttpRequest.class))).toReturn(response);
    }


    protected Request createValidRequest() {
        Request request = new Request();
        request.setMethod(Request.Method.GET);
        request.setPath("/mock/");
        return request;
    }

    protected Entry createValidEntry() {
        Entry entry = new Entry();
        entry.setResult("4");
        entry.setVariableA("2");
        entry.setVariableB("2");
        entry.setName("Mock");
        return entry;
    }

    protected Category createValidCategory() {
        Category category = new Category();
        category.setName("Test category");
        category.setRequest(createValidRequest());
        category.addEntry(createValidEntry());
        return category;
    }

    protected Suite createValidSuite() {
        Suite suite = new Suite();
        suite.setName("Test suite");
        suite.addCategory(createValidCategory());
        return suite;
    }
}
