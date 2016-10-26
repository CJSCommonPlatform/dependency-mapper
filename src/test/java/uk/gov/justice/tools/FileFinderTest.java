package uk.gov.justice.tools;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class FileFinderTest {

    FileFinder testObj;

    @Before
    public void setup() {
        Config config = new Config();
        testObj = new FileFinder(config);
    }

    @Test
    public void testFindPomFiles() {

        List<File> pomFiles = testObj.findPomFiles();

        assertThat(pomFiles.size(), is(0));
    }

    @Test
    public void testFindRamlFiles() {

        List<File> ramlFiles = testObj.findRamlFiles();

        assertThat(ramlFiles.size(), is(0));
    }
}