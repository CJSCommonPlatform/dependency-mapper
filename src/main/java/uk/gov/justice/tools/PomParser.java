package uk.gov.justice.tools;

import org.apache.maven.project.MavenProject;
import uk.gov.justice.builders.MicroService;
import uk.gov.justice.builders.MicroServiceBuilder;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

public class PomParser {

    private static String VERSION_NOT_AVAILABLE = "NA";
    private static String PROJECT_VERSION = "project.version";


    public MicroService parse(File pom) throws Exception {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader(pom));
        model.setPomFile(pom);

        //name
        String artifactId = model.getArtifactId();
        if (StringUtils.isBlank(artifactId))
            throw new PomParserException(pom, "ArtifactId is missing form pom file [" + pom.getAbsolutePath() + "]");

        //version
        String version = model.getVersion();
        version = StringUtils.isNotBlank(version) ? version : model.getParent().getVersion();

        MavenProject project = new MavenProject(model);
        Properties parentPomProperties = fetchParentPomProperties(pom);

        //uses
        List<MicroService> usesMicroServices = new ArrayList<MicroService>();
        Build build = model.getBuild();
        if (build != null) {
            build.getPlugins().forEach(plugin -> plugin.getDependencies()
                    .forEach(dependency -> addDependency(usesMicroServices, dependency, project, parentPomProperties)));
        }

        return new MicroServiceBuilder()
                .withName(artifactId)
                .withVersion(version)
                .withUses(usesMicroServices)
                .build();
    }

    private void addDependency(List<MicroService> usesMicroServices, Dependency dependency, MavenProject project, Properties parentProperties) {
        if (dependency != null && dependency.getClassifier() != null && dependency.getClassifier().equals("raml")) {
            usesMicroServices.add(new MicroServiceBuilder().withName(dependency.getArtifactId()).withVersion(fetchDependencyVersion(dependency.getVersion(), project, parentProperties)).build());
        }
    }

    private String fetchDependencyVersion(String dependencyVersion, MavenProject mavenProject, Properties parentPomProps) {

        // Check if no version specified
        if(dependencyVersion == null) {
            return VERSION_NOT_AVAILABLE;
        } else if(isVariable(dependencyVersion)) {
            String versionFromPom = parseVariableName(dependencyVersion);
            if(versionFromPom.equals(PROJECT_VERSION)) {
                    return mavenProject.getVersion();
            }else {
                Object propertyVersionValue = mavenProject.getProperties().get(versionFromPom);
                //deal with variable version from parent
                propertyVersionValue = propertyVersionValue == null ?
                        parentPomProps.getProperty(versionFromPom).toString() : propertyVersionValue;
                return propertyVersionValue != null ? propertyVersionValue.toString() : dependencyVersion;
            }
        } else {
            return dependencyVersion;
        }
    }

    private boolean isVariable(String version) {
        Pattern anyVariablePattern = Pattern.compile("([${]+.)\\w+");
        Matcher anyVariableMatcher = anyVariablePattern.matcher(version);
        return anyVariableMatcher.find();
    }

    private String parseVariableName(String version) {
        Pattern variableNamePattern = Pattern.compile("([a-z]+.)\\w+");
        Matcher variableNameMatcher = variableNamePattern.matcher(version);
        if(variableNameMatcher.find()) {
            return variableNameMatcher.group(0);
        } else {
            return VERSION_NOT_AVAILABLE;
        }
    }

    public Properties fetchParentPomProperties(File somePom) throws Exception {
        Properties props = new Properties();
        if(somePom != null && somePom.isFile()){
            File parent = somePom.getParentFile().getParentFile();
            if(parent.isDirectory()){
                File parentPom = new File(parent.getAbsolutePath().concat(File.separator).concat("pom.xml"));
                if (parentPom.isFile() && parentPom.exists()) {
                    MavenXpp3Reader reader = new MavenXpp3Reader();
                    Model model = reader.read(new FileReader(parentPom));
                    model.setPomFile(parentPom);
                    MavenProject project = new MavenProject(model);
                    return project.getProperties();
                }
            }
        }
        return props;
    }
}
