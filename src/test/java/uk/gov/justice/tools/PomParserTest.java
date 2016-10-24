package uk.gov.justice.tools;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class PomParserTest {

    @Test
    public void testParseNameFromPom() throws IOException, XmlPullParserException {
        PomParser actualPomParser = new PomParser();

        ClassLoader classLoader = getClass().getClassLoader();
        File somePom = new File(classLoader.getResource("./root/pom.xml").getFile());

        String actualName = actualPomParser.parse(somePom);

        assertThat(actualName, is("lifecycle-event-processor"));
    }
}
