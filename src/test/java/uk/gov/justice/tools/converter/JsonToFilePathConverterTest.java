package uk.gov.justice.tools.converter;


import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.tools.Config;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

public class JsonToFilePathConverterTest {

    @Test
    public void convert() throws Exception {
        Config config = new Config();
        config.setOutputDirectory(System.getProperty("java.io.tmpdir") + "/");


        JsonToFilePathConverter testObj = new JsonToFilePathConverter(config);

        Path path = testObj.convert("{\"name\":\"abc\",\"version\":\"1.1\"}");

        assertThat(new String(Files.readAllBytes(path)), is("{\"name\":\"abc\",\"version\":\"1.1\"}"));

    }

}