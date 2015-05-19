package neueda.homework;

import com.fasterxml.jackson.databind.ObjectMapper;
import neueda.homework.pojo.Category;
import neueda.homework.pojo.Entry;
import neueda.homework.pojo.Suite;
import neueda.homework.pojo.xml.MindMap;
import neueda.homework.util.MindMapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.runner.JUnitCore;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Kristaps Kohs
 */
public class Application {
    private static final Logger LOGGER = LogManager.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
      
    }
    
    
    /*  JAXBContext context = JAXBContext.newInstance(MindMap.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        MindMap map = (MindMap) unmarshaller.unmarshal(new FileInputStream("D:\\development\\projects\\used\\calc-test-suite\\src\\main\\resources\\calc_tests"));

        map.getNodes();

        List<Suite> suites = MindMapUtils.parseMindMap(map);


        for (Suite suite : suites) {
            LOGGER.info("Starting Suite - " + suite.getName());

            for (Category category : suite.getCategories()) {
                LOGGER.info("Executing " + category.getName() + " tests");
                for (Entry entry : category.getEntries()) {
                    LOGGER.info("Executing entry" + entry.getName() + " tests with values a=" + entry.getVariableA() +" b=" + entry.getVariableB());
                    URIBuilder builder = new URIBuilder(category.getRequest().getPath())
                            .addParameter("a", entry.getVariableA()).addParameter("b", entry.getVariableB());
                    CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
                    HttpRequest httpRequest = null;
                    switch (category.getRequest().getMethod()) {
                        case GET: {
                            httpRequest = new HttpGet(builder.build());
                        }
                        break;
                        case POST: {
                            httpRequest = new HttpPost(builder.build());
                        }
                        break;
                    }

                    HttpResponse response = closeableHttpClient.execute(new HttpHost("calculator.neueda.lv", 80, "http"), httpRequest);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    // response.getEntity().writeTo(outputStream);

                    String result = (String) new ObjectMapper().readValue(response.getEntity().getContent(), Map.class).get("result");
                    LOGGER.info("Response " +result);

                    if (StringUtils.equalsIgnoreCase(entry.getResult(), result)) {
                        LOGGER.info("Test " + entry.getName() + " success.");
                    } else {
                        LOGGER.info("Test " + entry.getName() + " failed.");
                    }
                }

            }
        }*/
}
