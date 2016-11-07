package uk.gov.justice.tools.service;

import org.junit.Test;
import uk.gov.justice.tools.Config;
import uk.gov.justice.tools.service.DependencyMapperService;

import static org.junit.Assert.assertTrue;

public class DependencyMapperServiceTest {

    @Test
    public void shouldProduceJsonBasedOnInputFolder() throws Exception {
        Config config = new Config();
        config.setRootDirectory("src/test/resources/root/");
        config.setOutputFilePath("NA");

        DependencyMapperService service = new DependencyMapperService(config);

        String actualJson = service.generate();
        assertTrue(!actualJson.isEmpty());
        System.out.println(actualJson);
    }

    @Test
    public void shouldNotProduceJsonWhenInputFolderHasNoPomFiles() throws Exception {
        Config config = new Config();
        config.setRootDirectory("src/test/resources/no_pom_here/");
        config.setOutputFilePath("NA");

        DependencyMapperService service = new DependencyMapperService(config);

        String actualJson = service.generate();
        assertTrue(actualJson.isEmpty());
    }
}
