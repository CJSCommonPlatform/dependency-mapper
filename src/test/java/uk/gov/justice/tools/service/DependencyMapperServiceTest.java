package uk.gov.justice.tools.service;

import org.junit.Test;
import uk.gov.justice.builders.MicroService;
import uk.gov.justice.builders.MicroServiceBuilder;
import uk.gov.justice.tools.Config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
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

    @Test
    public void shouldFetchRamlDocumentNameForMicroService() throws IOException {
        Config config = new Config();
        config.setRootDirectory("src/test/resources/microservice_project/");

        DependencyMapperService service = new DependencyMapperService(config);
        String expectedRamlDocReference = "progression-query-api.html";
        MicroService microService = new MicroServiceBuilder("progression-query-api", "2.0.55-SNAPSHOT").build();
        List<MicroService> microServices = Arrays.asList(microService);

        service.updateRamlDocumentReference(microServices);

        assertThat(microService.getRamlDocument(), is(expectedRamlDocReference));
    }

    @Test
    public void shouldSetRmlDocumentNotAvailableWhenNoRamlDocumentFoundInProject() throws IOException {
        Config config = new Config();
        config.setRootDirectory("src/test/resources/root/");

        DependencyMapperService service = new DependencyMapperService(config);
        String expectedRamlDocReference = "NA";
        MicroService microService = new MicroServiceBuilder("lifecycle-event-processor", "2.0.70-SNAPSHOT").build();
        List<MicroService> microServices = Arrays.asList(microService);

        service.updateRamlDocumentReference(microServices);

        assertThat(microService.getRamlDocument(), is(expectedRamlDocReference));
    }

}
