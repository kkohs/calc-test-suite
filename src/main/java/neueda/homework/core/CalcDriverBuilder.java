package neueda.homework.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Kristaps Kohs
 */
public class CalcDriverBuilder {
    private ObjectMapper objectMapper;
    private Logger logger;
    private HttpClient httpClient;
    private HttpHost httpHost;

    public CalcDriverBuilder setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        return this;
    }

    public CalcDriverBuilder setLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public CalcDriverBuilder setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    public CalcDriverBuilder setHttpHost(HttpHost httpHost) {
        this.httpHost = httpHost;
        return this;
    }

    public CalcDriverBuilder setHost(String host) {
        setHttpHost(new HttpHost(host));
        return this;
    }

    public CalcDriver build() {
        if (httpClient == null) {
            httpClient = HttpClients.createDefault();
        }

        if (logger == null) {
            logger = LogManager.getLogger(CalcDriver.class);
        }
        if (httpHost == null) {
            httpHost = new HttpHost("localhost");
        }

        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return new CalcDriver(logger, httpClient, httpHost, objectMapper);
    }
}
