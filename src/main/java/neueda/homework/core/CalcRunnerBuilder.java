package neueda.homework.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Builder class for building {@link neueda.homework.core.CalcEntryRunner}
 * @author Kristaps Kohs
 */
public class CalcRunnerBuilder {
    /**Default host name for runner.*/
    public static final String DEFAULT_HOST = "localhost";
    /**Json mapper.*/
    private ObjectMapper objectMapper;
    /**Logger.*/
    private Logger logger;
    /**HttpClient.*/
    private HttpClient httpClient;
    /**Host.*/
    private HttpHost httpHost;

    public CalcRunnerBuilder setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        return this;
    }

    public CalcRunnerBuilder setLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public CalcRunnerBuilder setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    public CalcRunnerBuilder setHttpHost(HttpHost httpHost) {
        this.httpHost = httpHost;
        return this;
    }

    public CalcRunnerBuilder setHost(String host) {
        setHttpHost(new HttpHost(host));
        return this;
    }

    /**
     * Builds new {@link neueda.homework.core.CalcEntryRunner}, 
     * default values are set for fields not set while assembling runner.
     * @return built runner.
     */
    public final CalcEntryRunner build() {
        if (httpClient == null) {
            httpClient = HttpClients.createDefault();
        }

        if (logger == null) {
            logger = LogManager.getLogger(CalcEntryRunner.class);
        }
        if (httpHost == null) {
            httpHost = new HttpHost(DEFAULT_HOST);
        }

        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return new CalcEntryRunner(logger, httpClient, httpHost, objectMapper);
    }
}
