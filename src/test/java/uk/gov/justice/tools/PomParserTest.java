package uk.gov.justice.tools;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.builders.MicroService;
import uk.gov.justice.builders.MicroServiceBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;


public class PomParserTest {

    private static final String ROOT_FOLDER = "root";
    private static final String BRANCH_FOLDER = "root/branch";
    private static final String FAULTY_FOLDER = "faulty";

    @Test
    public void shouldParseNameFromPom() throws Exception {
        PomParser actualPomParser = new PomParser();

        File somePom = getFileFromTestResources(ROOT_FOLDER);

        MicroService actualMicroService = actualPomParser.parse(somePom);

        assertThat(actualMicroService.getName(), is("lifecycle-event-processor"));
    }


    @Test
    public void shouldParseVersionFromPomWhenVersionPresent() throws Exception {
        PomParser actualPomParser = new PomParser();

        File somePom = getFileFromTestResources(ROOT_FOLDER);

        MicroService actualMicroService = actualPomParser.parse(somePom);

        assertThat(actualMicroService.getVersion(), is("2.0.70-SNAPSHOT"));
    }

    @Test
    public void shouldParseVersionFromParentInPomWhenVersionIsNotPresent() throws Exception {
        PomParser actualPomParser = new PomParser();

        File somePom = getFileFromTestResources(BRANCH_FOLDER);

        MicroService actualMicroService = actualPomParser.parse(somePom);

        assertThat(actualMicroService.getVersion(), is("2.0.70-SNAPSHOT"));
    }


    @Test
    public void shouldParseNameAndVersionFromPomAndReturnContext() throws Exception {
        PomParser actualPomParser = new PomParser();

        File somePom = getFileFromTestResources(ROOT_FOLDER);

        MicroService actualMicroService = actualPomParser.parse(somePom);

        MicroService microServiceExpected = new MicroServiceBuilder()
                .withName("lifecycle-event-processor")
                .withVersion("2.0.70-SNAPSHOT")
                .build();

        assertThat(actualMicroService, is(microServiceExpected));
    }


    @Test(expected = PomParserException.class)
    public void shouldThrowCheckedExceptionWhenNameIsNotPresent() throws Exception {
        PomParser actualPomParser = new PomParser();

        File somePom = getFileFromTestResources(FAULTY_FOLDER);

        actualPomParser.parse(somePom);
    }

    @Test
    public void shouldContainDependenciesIncludingVersions() throws Exception {
        PomParser actualPomParser = new PomParser();

        List<MicroService> expected = Arrays.asList(
                new MicroServiceBuilder().withName("people-command-api").withVersion("${people.version}").build(),
                new MicroServiceBuilder().withName("material-command-api").withVersion("${material.version}").build(),
                new MicroServiceBuilder().withName("structure-command-api").withVersion("${structure.version}").build(),
                new MicroServiceBuilder().withName("structure-event-listener").withVersion("${structure.version}").build(),
                new MicroServiceBuilder().withName("structure-query-api").withVersion("${structure.version}").build(),
                new MicroServiceBuilder().withName("charging-command-api").withVersion("${charging.version}").build(),
                new MicroServiceBuilder().withName("assignment-command-api").withVersion("${assignment.version}").build(),
                new MicroServiceBuilder().withName("charging-query-api").withVersion("${charging.version}").build(),
                new MicroServiceBuilder().withName("scheduling-command-api").withVersion("${scheduling.version}").build(),
                new MicroServiceBuilder().withName("scheduling-query-api").withVersion("${scheduling.version}").build(),
                new MicroServiceBuilder().withName("progression-command-api").withVersion("${progression.version}").build(),
                new MicroServiceBuilder().withName("progression-query-api").withVersion("${progression.version}").build()

        );

        File somePom = getFileFromTestResources(BRANCH_FOLDER);

        MicroService actualMicroService = actualPomParser.parse(somePom);

        assertThat(actualMicroService.uses(), is(expected));
    }


    private File getFileFromTestResources(String folderName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource("./".concat(folderName).concat("/pom.xml")).getFile());
    }


}
