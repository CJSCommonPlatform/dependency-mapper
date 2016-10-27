package uk.gov.justice;

import org.junit.Test;
import uk.gov.justice.tools.Config;

import static org.junit.Assert.assertTrue;

public class SystemDataServiceTest {

    @Test
    public void shouldProduceJsonBasedOnInputFolder() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String rootFolder = classLoader.getResource("./root").getPath();


        Config config = new Config();
        config.setRootDirectory(rootFolder);
        config.setOutputDirectory("NA");

        SystemDataService service = new SystemDataService(config);

        String actualJson = service.generate();
        assertTrue(!actualJson.isEmpty());
        System.out.println(actualJson);
    }
}
