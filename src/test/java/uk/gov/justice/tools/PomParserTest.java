package uk.gov.justice.tools;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Test;
import uk.gov.justice.builder.Context;
import uk.gov.justice.builder.ContextBuilder;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class PomParserTest {

    @Test
    public void shouldParseNameFromPom() throws IOException, XmlPullParserException {
        PomParser actualPomParser = new PomParser();

        File somePom = getFileFromTestResources();

        Context actualContext = actualPomParser.parse(somePom);

        assertThat(actualContext.getName(), is("lifecycle-event-processor"));
    }


    @Test
    public void shouldParseVersionFromPom() throws IOException, XmlPullParserException {
        PomParser actualPomParser = new PomParser();

        File somePom = getFileFromTestResources();

        Context actualContext = actualPomParser.parse(somePom);

        assertThat(actualContext.getVersion(), is("2.0.70-SNAPSHOT"));
    }


    @Test
    public void shouldParseNameAndVersionFromPomAndReturnContext() throws IOException, XmlPullParserException {
        PomParser actualPomParser = new PomParser();

        File somePom = getFileFromTestResources();

        Context actualContext = actualPomParser.parse(somePom);

        Context contextExpected = new ContextBuilder()
                .withName("lifecycle-event-processor")
                .withVersion("2.0.70-SNAPSHOT")
                .build();

        assertThat(actualContext, is(contextExpected));
    }

    private File getFileFromTestResources() {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource("./root/pom.xml").getFile());
    }


}
