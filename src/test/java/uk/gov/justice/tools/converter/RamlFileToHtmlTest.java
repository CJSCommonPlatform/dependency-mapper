package uk.gov.justice.tools.converter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.tools.Config;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class RamlFileToHtmlTest {

    private static final String RAML_FOLDER = "src/test/resources/raml/";

    @Test
    public void convert() throws Exception {
        String expectedHtmlText = new String(Files.readAllBytes(Paths.get("src/test/resources/raml/tohtml/assignment-command-api.html")));

        Config config = new Config();
        config.setRamlReportDirectory(RAML_FOLDER);

        RamlFileToHtml testObj = new RamlFileToHtml(config);

        Html result = testObj.convert(new File(RAML_FOLDER + "assignment-command-api.raml"));

        assertThat("src/test/resources/raml/assignment-command-api.html", is(result.getFilePath()));

        assertThat(expectedHtmlText, is(result.getHtmlAsText()));

    }

}