package uk.gov.justice;

import org.junit.Test;
import uk.gov.justice.tools.Config;
import uk.gov.justice.tools.DependencyMapperService;

import static org.junit.Assert.assertTrue;

public class DependencyMapperServiceTest {

    @Test
    public void shouldProduceJsonBasedOnInputFolder() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String rootFolder = classLoader.getResource("./root").getPath();

        Config config = new Config();
        config.setRootDirectory(rootFolder);
        config.setOutputFilePath("NA");

        DependencyMapperService service = new DependencyMapperService(config);

        String actualJson = service.generate();
        assertTrue(!actualJson.isEmpty());
        System.out.println(actualJson);
    }

    @Test
    public void shouldNotProduceJsonWhenInputFolderHasNoPomFiles() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String rootFolder = classLoader.getResource("./no_pom_here").getPath();

        Config config = new Config();
        config.setRootDirectory(rootFolder);
        config.setOutputFilePath("NA");

        DependencyMapperService service = new DependencyMapperService(config);

        String actualJson = service.generate();
        assertTrue(actualJson.isEmpty());
    }
}
