package uk.gov.justice.tools;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import uk.gov.justice.builders.MicroService;
import uk.gov.justice.builders.MicroServiceBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.Test;


public class PomParserTest {

    private static final String ROOT_FOLDER = "root";
    private static final String BRANCH_FOLDER = "root/branch";
    private static final String FAULTY_FOLDER = "faulty";
    private static final String NO_DEPENDENCY_VERSIONS_FOLDER = "no_dep_versions";
    private static final String DEPENDENCY_VERSIONS_SPECIFIED_FOLDER = "dep_versions_specified";
    private static final String PROJECT_VERSION_SPECIFIED_FOLDER = "project_version_specified";
    private static final String PARENT_HAVING_VERSION_VARIABLES_CHILD_FOLDER = "parent_has_version_variables/branch";
    private static final String PARENT_VERSION_WITH_SERVICE_LEVEL_PARENT_POM = "parent_has_version_variables";
    private static final String GRAND_PARENT_HAVING_VERSION_VARIABLES_CHILD_FOLDER = "grand_parent_has_version_variables/parent/child";
    private static final String PEOPLE_CONTEXT_VERSION = "2.0.23";
    private static final String MATERIAL_CONTEXT_VERSION = "2.0.18";
    private static final String STRUCTURE_CONTEXT_VERSION = "2.0.49";
    private static final String CHARGING_CONTEXT_VERSION = "2.0.85";
    private static final String ASSIGNMENT_CONTEXT_VERSION = "2.0.11";
    private static final String SCHEDULING_CONTEXT_VERSION = "2.0.24";
    private static final String PROGRESSION_CONTEXT_VERSION = "2.0.21";

    private static final String PEOPLE_CONTEXT = "people";
    private static final String MATERIAL_CONTEXT = "material";
    private static final String STRUCTURE_CONTEXT = "structure";
    private static final String CHARGING_CONTEXT = "charging";
    private static final String ASSIGNMENT_CONTEXT = "assignment";
    private static final String SCHEDULING_CONTEXT = "scheduling";
    private static final String PROGRESSION_CONTEXT = "progression";
    private static final String NO_VERSION = "NA";


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
    public void shouldParseParentPomProperties() throws Exception{
        PomParser actualPomParser = new PomParser();

        File somePom = getFileFromTestResources(BRANCH_FOLDER);

        Properties expectedProps = new Properties();
        expectedProps.put("people.version", PEOPLE_CONTEXT_VERSION);
        expectedProps.put("material.version", MATERIAL_CONTEXT_VERSION);
        expectedProps.put("structure.version", STRUCTURE_CONTEXT_VERSION);
        expectedProps.put("charging.version", CHARGING_CONTEXT_VERSION);
        expectedProps.put("assignment.version", ASSIGNMENT_CONTEXT_VERSION);
        expectedProps.put("scheduling.version", SCHEDULING_CONTEXT_VERSION);
        expectedProps.put("progression.version", PROGRESSION_CONTEXT_VERSION);
        expectedProps.put("cpp.service-component", "EVENT_PROCESSOR");
        expectedProps.put("activiti.version", "5.20.0");

        Properties actualProps = actualPomParser.fetchParentPomProperties(somePom);

        actualProps.stringPropertyNames().forEach(actualKey -> {
            assertTrue(expectedProps.containsKey(actualKey));
            assertThat(actualProps.getProperty(actualKey), is(expectedProps.getProperty(actualKey)));
        });
    }

    @Test
    public void shouldExtractVersionFromParent() throws Exception {
        PomParser actualPomParser = new PomParser();

        File somePom = getFileFromTestResources(BRANCH_FOLDER);

        String versionKey = "assignment.version";
        String expectedVersion = ASSIGNMENT_CONTEXT_VERSION;
        String actualVersion = actualPomParser.fetchVersionFromParents(somePom, versionKey);

        assertThat(actualVersion, is(expectedVersion));

    }

    @Test
    public void shouldExtractVersionFromGrandParent() throws Exception {
        PomParser actualPomParser = new PomParser();

        File somePom = getFileFromTestResources(GRAND_PARENT_HAVING_VERSION_VARIABLES_CHILD_FOLDER);

        String versionKey = "charging.version";
        String expectedVersion = "2.0.149";
        String actualVersion = actualPomParser.fetchVersionFromParents(somePom, versionKey);

        assertThat(actualVersion, is(expectedVersion));

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
    public void shouldResolveVersionDataFromVariables() throws Exception {
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
        assertDependenciesVersion(actualMicroService);
    }

    @Test
    public void shouldResolveVersionDataFromParentPomVariables() throws Exception {
        PomParser actualPomParser = new PomParser();

        String structureQueryViewVersion = "2.0.102-SNAPSHOT";
        String assignmentQueryApiVersion = "2.0.11";
        String chargingQueryApiVersion = "2.0.149";
        String peopleQueryApiVersion = "2.0.16";
        List<MicroService> expected = Arrays.asList(
                new MicroServiceBuilder().withName("structure-query-view").withVersion(structureQueryViewVersion).build(),
                new MicroServiceBuilder().withName("assignment-query-api").withVersion(assignmentQueryApiVersion).build(),
                new MicroServiceBuilder().withName("charging-query-api").withVersion(chargingQueryApiVersion).build(),
                new MicroServiceBuilder().withName("people-query-api").withVersion(peopleQueryApiVersion).build()
        );

        File somePom = getFileFromTestResources(PARENT_HAVING_VERSION_VARIABLES_CHILD_FOLDER);

        MicroService actualMicroService = actualPomParser.parse(somePom);
        assertThat(actualMicroService.uses(), is(expected));

        actualMicroService.uses().forEach(dep -> {
            if (dep.getName().startsWith(PEOPLE_CONTEXT)){
                assertThat(dep.getVersion(), is(peopleQueryApiVersion));
            } else if (dep.getName().startsWith(STRUCTURE_CONTEXT)){
                assertThat(dep.getVersion(), is(structureQueryViewVersion));
            } else if (dep.getName().startsWith(CHARGING_CONTEXT)){
                assertThat(dep.getVersion(), is(chargingQueryApiVersion));
            } else if (dep.getName().startsWith(ASSIGNMENT_CONTEXT)){
                assertThat(dep.getVersion(), is(assignmentQueryApiVersion));
            }
        });
    }

    @Test
    public void shouldResolveVersionDataFromGrandParentPomVariables() throws Exception {
        PomParser actualPomParser = new PomParser();

        String structureQueryViewVersion = "2.0.102-SNAPSHOT";
        String assignmentQueryApiVersion = "2.0.11";
        String chargingQueryApiVersion = "2.0.149";
        String peopleQueryApiVersion = "2.0.16";
        List<MicroService> expected = Arrays.asList(
                new MicroServiceBuilder().withName("structure-query-view").withVersion(structureQueryViewVersion).build(),
                new MicroServiceBuilder().withName("assignment-query-api").withVersion(assignmentQueryApiVersion).build(),
                new MicroServiceBuilder().withName("charging-query-api").withVersion(chargingQueryApiVersion).build(),
                new MicroServiceBuilder().withName("people-query-api").withVersion(peopleQueryApiVersion).build()
        );

        File somePom = getFileFromTestResources(GRAND_PARENT_HAVING_VERSION_VARIABLES_CHILD_FOLDER);

        MicroService actualMicroService = actualPomParser.parse(somePom);
        assertThat(actualMicroService.uses(), is(expected));

        actualMicroService.uses().forEach(dep -> {
            if (dep.getName().startsWith(PEOPLE_CONTEXT)){
                assertThat(dep.getVersion(), is(peopleQueryApiVersion));
            } else if (dep.getName().startsWith(STRUCTURE_CONTEXT)){
                assertThat(dep.getVersion(), is(structureQueryViewVersion));
            } else if (dep.getName().startsWith(CHARGING_CONTEXT)){
                assertThat(dep.getVersion(), is(chargingQueryApiVersion));
            } else if (dep.getName().startsWith(ASSIGNMENT_CONTEXT)){
                assertThat(dep.getVersion(), is(assignmentQueryApiVersion));
            }
        });
    }


    @Test
    public void shouldResolveVersionDataAsUnavailable() throws Exception {
        PomParser actualPomParser = new PomParser();

        List<MicroService> expected = Arrays.asList(
                new MicroServiceBuilder().withName("people-command-api").withVersion(NO_VERSION).build(),
                new MicroServiceBuilder().withName("material-command-api").withVersion(NO_VERSION).build(),
                new MicroServiceBuilder().withName("structure-command-api").withVersion(NO_VERSION).build(),
                new MicroServiceBuilder().withName("structure-query-api").withVersion(NO_VERSION).build(),
                new MicroServiceBuilder().withName("charging-command-api").withVersion(NO_VERSION).build(),
                new MicroServiceBuilder().withName("assignment-command-api").withVersion(NO_VERSION).build(),
                new MicroServiceBuilder().withName("charging-query-api").withVersion(NO_VERSION).build(),
                new MicroServiceBuilder().withName("scheduling-command-api").withVersion(NO_VERSION).build(),
                new MicroServiceBuilder().withName("scheduling-query-api").withVersion(NO_VERSION).build(),
                new MicroServiceBuilder().withName("progression-command-api").withVersion(NO_VERSION).build(),
                new MicroServiceBuilder().withName("progression-query-api").withVersion(NO_VERSION).build()
        );

        File somePom = getFileFromTestResources(NO_DEPENDENCY_VERSIONS_FOLDER);

        MicroService actualMicroService = actualPomParser.parse(somePom);

        assertThat(actualMicroService.uses(), is(expected));
        actualMicroService.uses().forEach(dep -> assertThat(dep.getVersion(), is("NA")));
    }

    @Test
    public void shouldResolveVersionDataFromVersionValues() throws Exception {
        PomParser actualPomParser = new PomParser();

        List<MicroService> expected = Arrays.asList(
                new MicroServiceBuilder().withName("people-command-api").withVersion(PEOPLE_CONTEXT_VERSION).build(),
                new MicroServiceBuilder().withName("material-command-api").withVersion(MATERIAL_CONTEXT_VERSION).build(),
                new MicroServiceBuilder().withName("structure-command-api").withVersion(STRUCTURE_CONTEXT_VERSION).build(),
                new MicroServiceBuilder().withName("structure-query-api").withVersion(STRUCTURE_CONTEXT_VERSION).build(),
                new MicroServiceBuilder().withName("charging-command-api").withVersion(CHARGING_CONTEXT_VERSION).build(),
                new MicroServiceBuilder().withName("assignment-command-api").withVersion(ASSIGNMENT_CONTEXT_VERSION).build(),
                new MicroServiceBuilder().withName("charging-query-api").withVersion(CHARGING_CONTEXT_VERSION).build(),
                new MicroServiceBuilder().withName("scheduling-command-api").withVersion(SCHEDULING_CONTEXT_VERSION).build(),
                new MicroServiceBuilder().withName("scheduling-query-api").withVersion(SCHEDULING_CONTEXT_VERSION).build(),
                new MicroServiceBuilder().withName("progression-command-api").withVersion(PROGRESSION_CONTEXT_VERSION).build(),
                new MicroServiceBuilder().withName("progression-query-api").withVersion(PROGRESSION_CONTEXT_VERSION).build()
        );

        File somePom = getFileFromTestResources(DEPENDENCY_VERSIONS_SPECIFIED_FOLDER);

        MicroService actualMicroService = actualPomParser.parse(somePom);

        assertThat(actualMicroService.uses(), is(expected));
        assertDependenciesVersion(actualMicroService);

    }

    @Test
    public void shouldResolveVersionDataFromProjectVersionVariable() throws Exception {
        PomParser actualPomParser = new PomParser();

        String expected_dep_version = "2.0.33-SNAPSHOT";
        List<MicroService> expected = Arrays.asList(
                new MicroServiceBuilder().withName("assignment-query-controller").withVersion(expected_dep_version).build());

        File somePom = getFileFromTestResources(PROJECT_VERSION_SPECIFIED_FOLDER);

        MicroService actualMicroService = actualPomParser.parse(somePom);

        assertThat(actualMicroService.uses(), is(expected));
        actualMicroService.uses().forEach(dep -> assertThat(dep.getVersion(), is(expected_dep_version)));
    }

    @Test
    public void shouldResolveServiceParentPomVersionVariable() throws Exception {
        PomParser actualPomParser = new PomParser();

        String expectedServiceParentPomVerison = "2.0.27";

        File somePom = getFileFromTestResources(PARENT_VERSION_WITH_SERVICE_LEVEL_PARENT_POM);
        MicroService actualMicroService = actualPomParser.parse(somePom);

        assertThat(actualMicroService.getServicePomVersion(), is(expectedServiceParentPomVerison));
    }

    @Test
    public void shouldResolveServiceParentPomVersionVariableFromParentPom() throws Exception {
        PomParser actualPomParser = new PomParser();

        String expectedServiceParentPomVerison = "2.0.27";

        File somePom = getFileFromTestResources(PARENT_HAVING_VERSION_VARIABLES_CHILD_FOLDER);
        MicroService actualMicroService = actualPomParser.parse(somePom);

        assertThat(actualMicroService.getServicePomVersion(), is(expectedServiceParentPomVerison));
    }

    private void assertDependenciesVersion(MicroService actualMicroService) {
        actualMicroService.uses().forEach(this::assertDependency);
    }

    private void assertDependency(MicroService dep) {
        if (dep.getName().startsWith(PEOPLE_CONTEXT)){
            assertThat(dep.getVersion(), is(PEOPLE_CONTEXT_VERSION));
            return;
        }
        if (dep.getName().startsWith(MATERIAL_CONTEXT)) {
            assertThat(dep.getVersion(), is(MATERIAL_CONTEXT_VERSION));
            return;
        }
        if (dep.getName().startsWith(STRUCTURE_CONTEXT)) {
            assertThat(dep.getVersion(), is(STRUCTURE_CONTEXT_VERSION));
            return;
        }
        if (dep.getName().startsWith(CHARGING_CONTEXT)) {
            assertThat(dep.getVersion(), is(CHARGING_CONTEXT_VERSION));
            return;
        }
        if (dep.getName().startsWith(ASSIGNMENT_CONTEXT)) {
            assertThat(dep.getVersion(), is(ASSIGNMENT_CONTEXT_VERSION));
            return;
        }
        if (dep.getName().startsWith(SCHEDULING_CONTEXT)) {
            assertThat(dep.getVersion(), is(SCHEDULING_CONTEXT_VERSION));
            return;
        }
        if (dep.getName().startsWith(PROGRESSION_CONTEXT)) {
            assertThat(dep.getVersion(), is(PROGRESSION_CONTEXT_VERSION));
        }
    }


    private File getFileFromTestResources(String folderName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource("./".concat(folderName).concat("/pom.xml")).getFile());
    }


}
