package neueda.homework.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import neueda.homework.pojo.*;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Kristaps Kohs
 */
public class CalcDriver implements AutoCloseable {
    private final Logger logger;
    private final HttpClient client;
    private final HttpHost endpoint;
    private final ObjectMapper mapper;

    public CalcDriver(Logger logger, HttpClient client, HttpHost endpoint, ObjectMapper mapper) {
        this.logger = logger;
        this.client = client;
        this.endpoint = endpoint;
        this.mapper = mapper;
    }

    public List<Result> runCompleteSuite(final Suite suite) throws Exception {
        final List<Result> results = new ArrayList<>();
        logger.info("Starting complete suite[" + suite.getName() + "] tests");
        for (Category category : suite.getCategories()) {
            logger.info("Processing category [" + category.getName() + "]");
            for (Entry entry : category.getEntries()) {
                results.add(runSingleEntry(category.getRequest(), entry));
            }
        }
        return results;
    }

    public Result runSingleEntry(final Request request, final Entry entry) throws Exception {
        logger.info("Executing entry " + entry.getName() + " against " + endpoint.toURI() + request.getPath());
        final Result result = new Result(entry.getName(), entry.getResult());
        HttpResponse response = null;
        try {
            final HttpRequest httpRequest = prepareMethod(request, entry);

            response = client.execute(endpoint, httpRequest);
            final StatusLine statusLine = response.getStatusLine();
            logger.info("Received response " + statusLine.getReasonPhrase());

            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                final Map resultMap = mapper.readValue(response.getEntity().getContent(), Map.class);
                final Object resultValue = resultMap.get("result");
                if (resultValue == null) {
                    result.setError("No result received");
                } else {
                    result.setActual(resultValue);
                }

            } else {
                result.setError("Invalid HTTP response " + statusLine.getReasonPhrase());
            }

        } catch (UnknownHostException e) {
            result.setError("Unable to reach endpoint " + e.getMessage());
        } catch (Exception e) {
            result.setError(e.getMessage());
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
        return result;
    }

    private HttpRequest prepareMethod(Request request, Entry entry) throws URISyntaxException {
        final URI uri = new URIBuilder(request.getPath())
                .addParameter("a", entry.getVariableA())
                .addParameter("b", entry.getVariableB())
                .build();

        HttpRequest httpRequest;
        switch (request.getMethod()) {
            case GET: {
                httpRequest = new HttpGet(uri);
            }
            break;
            case POST: {
                httpRequest = new HttpPost(uri);
            }
            break;
            default: {
                throw new IllegalArgumentException("Unknown HTTP method " + request.getMethod() + " provided");
            }
        }
        return httpRequest;
    }


    @Override
    public void close() throws IOException {
        HttpClientUtils.closeQuietly(client);
    }
}
