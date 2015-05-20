package neueda.homework;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import neueda.homework.core.CalcEntryRunner;
import neueda.homework.core.CalcRunnerBuilder;
import neueda.homework.model.Result;
import neueda.homework.model.Suite;
import neueda.homework.model.xml.MindMap;
import neueda.homework.util.MindMapUtils;
import neueda.homework.util.SuiteUtils;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Console runner to execute tests via command line.
 *
 * @author Kristaps Kohs
 */
public class ConsoleRunner {
    private static final Logger LOGGER = LogManager.getLogger(ConsoleRunner.class);

    public static void main(String[] args) throws Exception {
        final OptionParser parser = new OptionParser();
        final OptionSpec<String> host = parser.acceptsAll(asList("host", "h"), "hostname of service")
                .withRequiredArg().ofType(String.class).defaultsTo("calculator.neueda.lv");
        final OptionSpec<String> mindMapFile = parser.acceptsAll(asList("mindmap", "f"), "Mind map file to generate tests")
                .withRequiredArg().ofType(String.class).required();
        parser.acceptsAll(asList("?", "help"), "show this help").forHelp();

        final OptionSet options;
        try {
            options = parser.parse(args);
        } catch (OptionException e) {
            parser.printHelpOn(System.err);
            System.err.println("Problem with arguments: " + e.getMessage());
            return;
        }

        if (options.has("help")) {
            parser.printHelpOn(System.err);
            System.exit(0);
        }

        new ConsoleRunner().start(options.valueOf(host), options.valueOf(mindMapFile));
    }

    /**
     * Starts execution.
     *
     * @param host        host.
     * @param mindMapFile test mind map file.
     * @throws Exception
     */
    public void start(final String host, final String mindMapFile) throws Exception {
        try (InputStream stream = FileUtils.openInputStream(FileUtils.getFile(mindMapFile))) {
            final MindMap mindMap = MindMapUtils.loadMinMap(stream);
            final List<Suite> suites = MindMapUtils.parseMindMap(mindMap);

            for (Suite suite : suites) {
                if (!SuiteUtils.validateSuite(suite)) {
                    throw new InvalidPropertiesFormatException("Suite " + suite.getName()
                            + " contains invalid or missing properties");
                }
                try (CalcEntryRunner calcEntryRunner = new CalcRunnerBuilder().setHost(host).build()) {
                    final List<Result> results = calcEntryRunner.runCompleteSuite(suite);
                    for (Result result : results) {
                        if (result.hasErrors()) {
                            LOGGER.error("Entry " + result.getProcessName() + " contained errors " + result.getError());
                        } else if (!result.matches()) {
                            LOGGER.error("Entry " + result.getProcessName()
                                    + " expected[" + result.getExpected()
                                    + "] result did not match actual[" + result.getActual() + "]");
                        }
                    }
                }
            }
        }
    }
}
