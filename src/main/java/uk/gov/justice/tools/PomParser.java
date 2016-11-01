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

    public MicroService parse(File pom) throws Exception {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader(pom));
        model.setPomFile(pom);

        //name
        String artifactId = model.getArtifactId();
        if (StringUtils.isBlank(artifactId))
            throw new PomParserException(pom, "ArtifactId is missing");

        //version
        String version = model.getVersion();
        version = StringUtils.isNotBlank(version) ? version : model.getParent().getVersion();

        MavenProject project = new MavenProject(model);
        Properties properties = project.getProperties();

        //uses
        List<MicroService> usesMicroServices = new ArrayList<MicroService>();
        Build build = model.getBuild();
        if (build != null) {
            build.getPlugins().forEach(plugin -> plugin.getDependencies()
                    .forEach(dependency -> addDependency(usesMicroServices, dependency, properties)));
        }

        return new MicroServiceBuilder()
                .withName(artifactId)
                .withVersion(version)
                .withUses(usesMicroServices)
                .build();
    }

    private void addDependency(List<MicroService> usesMicroServices, Dependency dependency, Properties properties) {
        if (dependency != null && dependency.getClassifier() != null && dependency.getClassifier().equals("raml")) {
            usesMicroServices.add(new MicroServiceBuilder().withName(dependency.getArtifactId()).withVersion(fetchDependencyVersion(dependency.getVersion(), properties)).build());
        }
    }

    private String fetchDependencyVersion(String version, Properties properties) {

        // Check if no version specified
        if(version == null) {
            return VERSION_NOT_AVAILABLE;
        } else if(isVariable(version)) {
            Object propertyVersionValue = properties.get(parseVariableName(version));
            return propertyVersionValue != null ? propertyVersionValue.toString() : version;
        } else {
            return version;
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
}
