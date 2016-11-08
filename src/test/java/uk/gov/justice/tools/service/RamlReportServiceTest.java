package uk.gov.justice.tools.service;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import uk.gov.justice.tools.Config;
import uk.gov.justice.tools.TempDirectoryUtil;


public class RamlReportServiceTest {
    @Test
    public void shouldGenerateRamlReport() throws Exception {

        final Config config = new Config();
        final Path tempDirectory = TempDirectoryUtil.createTempDirectory("dmxRamlReport");
        config.setRamlReportDirectory(tempDirectory.normalize().toString() + "/");
        config.setRootDirectory("src/test/resources/raml/");

        final RamlReportService testObj = new RamlReportService(config);
        testObj.generateRamlReport();

        final String actualdHtmlText = new String(Files.readAllBytes(Paths.get(config.getRamlReportDirectory() +"assignment-command-api.html"))).replaceAll("[^A-Za-z0-9]", "");

        final String expectedHtmlText = new String(Files.readAllBytes(Paths.get("src/test/resources/raml/tohtml/assignment-command-api.html"))).replaceAll("[^A-Za-z0-9]", "");

        assertThat(expectedHtmlText, is(actualdHtmlText));

        TempDirectoryUtil.recursiveDeleteOnExit(tempDirectory);
    }

}