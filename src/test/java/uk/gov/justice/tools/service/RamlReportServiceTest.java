package uk.gov.justice.tools.service;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import uk.gov.justice.tools.Config;
import uk.gov.justice.tools.TempDirectoryUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;


public class RamlReportServiceTest {
    @Test
    public void generateRamlReport() throws Exception {

        Config config = new Config();
        Path tempDirectory = TempDirectoryUtil.createTempDirectory("dmxRamlReport");
        config.setRamlReportDirectory(tempDirectory.normalize().toString() + "/");
        config.setRootDirectory("src/test/resources/raml/");

        RamlReportService testObj = new RamlReportService(config);
        testObj.generateRamlReport();

        String actualdHtmlText = new String(Files.readAllBytes(Paths.get(config.getRamlReportDirectory() +"assignment-command-api.html")));

        String expectedHtmlText = new String(Files.readAllBytes(Paths.get("src/test/resources/raml/tohtml/assignment-command-api.html")));

        assertThat(expectedHtmlText, is(actualdHtmlText));

        TempDirectoryUtil.recursiveDeleteOnExit(tempDirectory);
    }

}