package uk.gov.justice.tools;

import org.junit.Test;
import uk.gov.justice.builder.Context;
import uk.gov.justice.builder.ContextBuilder;

import java.io.File;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class PomParserTest {

        private static final String ROOT_FOLDER = "root";
        private static final String FAULTY_FOLDER = "faulty";

    @Test
    public void shouldParseNameFromPom() throws Exception {
        PomParser actualPomParser = new PomParser();

        File somePom = getFileFromTestResources(ROOT_FOLDER);

        Context actualContext = actualPomParser.parse(somePom);

        assertThat(actualContext.getName(), is("lifecycle-event-processor"));
    }


    @Test
    public void shouldParseVersionFromPomWhenVersionPresent() throws Exception {
        PomParser actualPomParser = new PomParser();

        File somePom = getFileFromTestResources(ROOT_FOLDER);

        Context actualContext = actualPomParser.parse(somePom);

        assertThat(actualContext.getVersion(), is("2.0.70-SNAPSHOT"));
    }

    //@Test
    public void shouldParseVersionFromParentInPomWhenVersionIsNotPresent() throws Exception {
        PomParser actualPomParser = new PomParser();

        File somePom = getFileFromTestResources(FAULTY_FOLDER);

        Context actualContext = actualPomParser.parse(somePom);

        assertThat(actualContext.getVersion(), is("2.0.70-SNAPSHOT"));
    }


    @Test
    public void shouldParseNameAndVersionFromPomAndReturnContext() throws Exception {
        PomParser actualPomParser = new PomParser();

        File somePom = getFileFromTestResources(ROOT_FOLDER);

        Context actualContext = actualPomParser.parse(somePom);

        Context contextExpected = new ContextBuilder()
                .withName("lifecycle-event-processor")
                .withVersion("2.0.70-SNAPSHOT")
                .build();

        assertThat(actualContext, is(contextExpected));
    }


    @Test(expected = PomParserException.class)
    public void shouldThrowCheckedExceptionWhenNameIsNotPresent() throws Exception {
        PomParser actualPomParser = new PomParser();

        File somePom = getFileFromTestResources(FAULTY_FOLDER);

        actualPomParser.parse(somePom);
    }


    private File getFileFromTestResources(String folderName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource("./".concat(folderName).concat("/pom.xml")).getFile());
    }


}
