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
    public void shouldParseNameAndVersionFromPom() throws IOException, XmlPullParserException {
        PomParser actualPomParser = new PomParser();

        ClassLoader classLoader = getClass().getClassLoader();
        File somePom = new File(classLoader.getResource("./root/pom.xml").getFile());

        Context actualContext = actualPomParser.parse(somePom);

        Context contextExpected = new ContextBuilder()
                .withName("lifecycle-event-processor")
                .withVersion("2.0.70-SNAPSHOT")
                .build();

        assertThat(actualContext, is(contextExpected));
    }

}
