package uk.gov.justice.tools;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Test;

public class PomParserExceptionTest {

    @Test
    public void shouldReportFileAndMessage() {
        String errorMessage = "some error message";
        ClassLoader classLoader = getClass().getClassLoader();
        File someFile = new File(classLoader.getResource("./faulty/pom.xml").getFile());

        try {

            throw new PomParserException(someFile, errorMessage);
        } catch (PomParserException e) {
            String expectedErrorMessage = "Error encountered when processing file ' " + someFile.getAbsolutePath() + " ' --> " + errorMessage;
            assertThat(e.getMessage(), is(expectedErrorMessage));
        }
    }

    @Test
    public void shouldReportMessageOnly() {
        String errorMessage = "some error message";
        try {

            throw new PomParserException(null, errorMessage);
        } catch (PomParserException e) {
            assertThat(e.getMessage(), is(errorMessage));
        }
    }

    @Test
    public void shouldReportDefaultMessageOnly() {
        try {
            throw new PomParserException(null, null);
        } catch (PomParserException e) {
            assertThat(e.getMessage(), is(PomParserException.DEFAULT_MESSAGE));
        }
    }
}
