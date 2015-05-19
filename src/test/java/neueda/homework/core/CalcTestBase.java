package neueda.homework.core;

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
}
