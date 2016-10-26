package uk.gov.justice.tools;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class FileFinderTest {

    private static final String ROOT_FOLDER = "root";

    FileFinder testObj;

    @Before
    public void setup() {
        ClassLoader classLoader = getClass().getClassLoader();
        Config config = new Config();
        config.setRootDirectory(classLoader.getResource("./".concat(ROOT_FOLDER)).getPath());
        testObj = new FileFinder(config);
    }

    @Test
    public void testFindPomFiles() throws IOException {
        List<File> pomFiles = testObj.findPomFiles();
        assertThat(pomFiles.size(), is(2));
    }

    @Test
    public void testFindRamlFiles() throws IOException {
        List<File> ramlFiles = testObj.findRamlFiles();
        assertThat(ramlFiles.size(), is(0));
    }
}