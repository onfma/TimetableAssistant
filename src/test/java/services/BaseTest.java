package services;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import static org.junit.jupiter.api.Assertions.*;

public class BaseTest {
    private static String CLASSPATH = "classpath:services";

    public void runKarateTests(String tag, int parallelCount) {
        Results results = Runner.path(CLASSPATH)
                .outputCucumberJson(true)
                .outputJunitXml(true)
                .tags(tag)
                .parallel(parallelCount);
        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }
}
