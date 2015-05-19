package neueda.homework;

import neueda.homework.core.CalcDriver;
import neueda.homework.core.CalcDriverBuilder;
import neueda.homework.pojo.Suite;
import neueda.homework.pojo.xml.MindMap;
import neueda.homework.util.MindMapUtils;
import org.apache.http.client.utils.URIBuilder;

import java.io.FileInputStream;
import java.util.List;

/**
 * @author Kristaps Kohs
 */
public class ConsoleRunner {
    public static void main(String[] args) throws Exception{
        CalcDriver calcDriver = new CalcDriverBuilder().setHost("calculator.neueda.lv").build();

        List<Suite> suites = MindMapUtils.parseMindMap(MindMapUtils.loadMinMap(new FileInputStream("D:\\Develoment\\Projects\\neueda-homework\\src\\main\\resources\\calc_tests")));


        for (Suite suite : suites) {
            calcDriver.runCompleteSuite(suite);
        }
    }
}
