package uk.gov.justice.tools;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.builders.MicroService;
import uk.gov.justice.builders.MicroServiceBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.junit.Test;


public class PomParserTest {

    private static final String ROOT_FOLDER = "root";
    private static final String BRANCH_FOLDER = "root/branch";
    private static final String FAULTY_FOLDER = "faulty";
    public static final String PEOPLE_CONTEXT_VERSION = "2.0.23";
    public static final String MATERIAL_CONTEXT_VERSION = "2.0.18";
    public static final String STRUCTURE_CONTEXT_VERSION = "2.0.49";
    public static final String CHARGING_CONTEXT_VERSION = "2.0.85";
    public static final String ASSIGNMENT_CONTEXT_VERSION = "2.0.11";
    public static final String SCHEDULING_CONTEXT_VERSION = "2.0.24";
    public static final String PROGRESSION_CONTEXT_VERSION = "2.0.21";
    public static final String PEOPLE_CONTEXT = "people";
    public static final String MATERIAL_CONTEXT = "material";
    public static final String STRUCTURE_CONTEXT = "structure";
    public static final String CHARGING_CONTEXT = "charging";
    public static final String ASSIGNMENT_CONTEXT = "assignment";
    public static final String SCHEDULING_CONTEXT = "scheduling";
    public static final String PROGRESSION_CONTEXT = "progression";

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
                new MicroServiceBuilder().withName("people-command-api").withVersion(PEOPLE_CONTEXT_VERSION).build(),
                new MicroServiceBuilder().withName("material-command-api").withVersion(MATERIAL_CONTEXT_VERSION).build(),
                new MicroServiceBuilder().withName("structure-command-api").withVersion(STRUCTURE_CONTEXT_VERSION).build(),
                new MicroServiceBuilder().withName("structure-event-listener").withVersion(STRUCTURE_CONTEXT_VERSION).build(),
                new MicroServiceBuilder().withName("structure-query-api").withVersion(STRUCTURE_CONTEXT_VERSION).build(),
                new MicroServiceBuilder().withName("charging-command-api").withVersion(CHARGING_CONTEXT_VERSION).build(),
                new MicroServiceBuilder().withName("assignment-command-api").withVersion(ASSIGNMENT_CONTEXT_VERSION).build(),
                new MicroServiceBuilder().withName("charging-query-api").withVersion(CHARGING_CONTEXT_VERSION).build(),
                new MicroServiceBuilder().withName("scheduling-command-api").withVersion(SCHEDULING_CONTEXT_VERSION).build(),
                new MicroServiceBuilder().withName("scheduling-query-api").withVersion(SCHEDULING_CONTEXT_VERSION).build(),
                new MicroServiceBuilder().withName("progression-command-api").withVersion(PROGRESSION_CONTEXT_VERSION).build(),
                new MicroServiceBuilder().withName("progression-query-api").withVersion(PROGRESSION_CONTEXT_VERSION).build()

        );

        File somePom = getFileFromTestResources(BRANCH_FOLDER);

        MicroService actualMicroService = actualPomParser.parse(somePom);

        assertThat(actualMicroService.uses(), is(expected));
    }

    private void assertDependenciesVersion(MicroService actualMicroService) {
    }


    private File getFileFromTestResources(String folderName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource("./".concat(folderName).concat("/pom.xml")).getFile());
    }


}
