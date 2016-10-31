package uk.gov.justice.tools.converter;


import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.tools.Config;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

public class WriteJsonTest {

    @Test
    public void convert() throws Exception {

        Path tempDir = Files.createTempDirectory("dmx");

        Config config = new Config();
        config.setOutputFilePath(tempDir.toString() + "/contexts.json");

        WriteJson testObj = new WriteJson(config);

        Path path = testObj.convert("{\"name\":\"abc\",\"version\":\"1.1\"}");

        assertThat(new String(Files.readAllBytes(path)), is("{\"name\":\"abc\",\"version\":\"1.1\"}"));

        //delete temp dir?
    }

}