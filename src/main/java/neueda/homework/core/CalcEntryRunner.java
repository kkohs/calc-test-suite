package neueda.homework.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import neueda.homework.model.Category;
import neueda.homework.model.Entry;
import neueda.homework.model.Request;
import neueda.homework.model.Result;
import neueda.homework.model.Suite;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Calculator test entry runner. This class can perform single entry tests against provided service
 * or run a complete suite.
 *
 * @author Kristaps Kohs
 */
public class CalcEntryRunner implements AutoCloseable {
    /** Logger. */
    private final Logger logger;
    /** HTTP client to execute request. */
    private final HttpClient client;
    /** REST service endpoint. */
    private final HttpHost endpoint;
    /** JSon mapper for results. */
    private final ObjectMapper mapper;

    /**
     * Constructs new runner for executing single entry or complete suite calculator tests against given REST service.
     *
     * @param logger   logger.
     * @param client   http client.
     * @param endpoint host.
     * @param mapper   json mapper.
     */
    CalcEntryRunner(final Logger logger, final HttpClient client, final HttpHost endpoint, final ObjectMapper mapper) {
        this.logger = logger;
        this.client = client;
        this.endpoint = endpoint;
        this.mapper = mapper;
    }

    /**
     * Performs complete suite execution, traverses through each category of suite and executes its entries.
     * After execution is complete returns list of results for each entry.
     *
     * @param suite to run.
     * @return list results
     */
    public final List<Result> runCompleteSuite(final Suite suite) {
        final List<Result> results = new ArrayList<>();
        logger.info("Starting complete suite[" + suite.getName() + "] tests");
        for (Category category : suite.getCategories()) {
            logger.info("Processing category [" + category.getCategoryName() + "]");
            for (Entry entry : category.getEntries()) {
                results.add(runSingleEntry(category.getRequest(), entry));
            }
        }
        logger.info("Execution complete");
        return results;
    }

    /**
     * Runs single entry operation against end service and returns result of operation.
     *
     * @param request request info.
     * @param entry   entry to run.
     * @return result.
     */
    public final Result runSingleEntry(final Request request, final Entry entry) {
        logger.info("Executing entry " + entry.getName() + " against " + endpoint.toURI() + request.getPath());
        final Result result = new Result(entry.getName(), entry.getResult());
        HttpResponse response = null;
        try {
            final HttpRequest httpRequest = prepareMethod(request, entry);

            response = client.execute(endpoint, httpRequest);
            final StatusLine statusLine = response.getStatusLine();
            logger.debug("Received response " + statusLine.getReasonPhrase());

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

    /**
     * Prepares HTTP method for execution.
     *
     * @param request request info.
     * @param entry   entry to run.
     * @return http request.
     * @throws URISyntaxException if faulty URI.
     */
    private HttpRequest prepareMethod(final Request request, final Entry entry) throws URISyntaxException {
        final URIBuilder uriBuilder = new URIBuilder(request.getPath());

        HttpRequest httpRequest;
        switch (request.getMethod()) {
            case GET: {
                uriBuilder.addParameter("a", entry.getVariableA())
                        .addParameter("b", entry.getVariableB());
                httpRequest = new HttpGet(uriBuilder.build());
            }
            break;
            default: {
                throw new IllegalArgumentException("Unknown HTTP method " + request.getMethod() + " provided");
            }
        }
        return httpRequest;
    }


    @Override
    public final void close() throws IOException {
        HttpClientUtils.closeQuietly(client);
    }
}
